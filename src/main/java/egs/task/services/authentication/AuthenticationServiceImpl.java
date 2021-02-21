package egs.task.services.authentication;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.user.SendCodeDto;
import egs.task.models.dtos.user.SendCodeTextDto;
import egs.task.models.dtos.user.UserResetPasswordDto;
import egs.task.models.dtos.user.UserVerifyCodeDto;
import egs.task.models.entities.Guest;
import egs.task.models.entities.Role;
import egs.task.models.entities.User;
import egs.task.models.entities.UserDevice;
import egs.task.repositories.GuestRepository;
import egs.task.repositories.RoleRepository;
import egs.task.repositories.UserRepository;
import egs.task.services.AbstractService;
import egs.task.services.user.UserService;
import egs.task.services.userDevice.UserDeviceService;
import egs.task.utils.GenerateCodeUtil;
import egs.task.utils.RoleConstants;
import egs.task.utils.SendEmail;
import egs.task.utils.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static egs.task.utils.AppConstants.*;


@Service
@Transactional
public class AuthenticationServiceImpl extends AbstractService<User, UserRepository> implements AuthenticationService {
    @Lazy
    private final UserRepository repository;
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final UserService userService;
    @Lazy
    private final UserDeviceService userDeviceService;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Lazy
    private final GuestRepository guestRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository repository, RoleRepository roleRepository, UserService userService,
                                     UserDeviceService userDeviceService, PasswordEncoder passwordEncoder,
                                     GuestRepository guestRepository) {
        super(repository);
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userDeviceService = userDeviceService;
        this.passwordEncoder = passwordEncoder;
        this.guestRepository = guestRepository;
    }

    @Override
    public Boolean sendCodeForSignUp(SendCodeDto sendCodeDto, String languageName) {
        User user = null;
        String code = GenerateCodeUtil.generateCode(4);
        if (sendCodeDto.getPhoneOrEmail().contains("@")) {
            Optional<User> optionalUser = repository.findByEmailAndHiddenFalse(sendCodeDto.getPhoneOrEmail());
            if (optionalUser.isPresent() && optionalUser.get().getFirstName() != null && optionalUser.get().getPassword() != null) {
                throw new IllegalArgumentException("An account with this Email already exists");
            } else if (optionalUser.isPresent() && optionalUser.get().getFirstName() == null && optionalUser.get().getPassword() == null) {
                user = optionalUser.get();
            } else if (optionalUser.isEmpty()) {
                user = new User();
            }
            assert user != null;
            user.setEmail(sendCodeDto.getPhoneOrEmail());
            user.setResetNumber(code);
            sendCodeToEmail(sendCodeDto.getPhoneOrEmail(), code, languageName);
        } else {
            Optional<User> optionalUser = repository.findByPhoneNumberAndHiddenFalse(sendCodeDto.getPhoneOrEmail());
            if (optionalUser.isPresent() && optionalUser.get().getFirstName() != null && optionalUser.get().getPassword() != null) {
                throw new IllegalArgumentException("An account with this phone number already exists");
            } else if (optionalUser.isPresent() && optionalUser.get().getFirstName() == null && optionalUser.get().getPassword() == null) {
                user = optionalUser.get();
            } else if (optionalUser.isEmpty()) {
                user = new User();
            }
            user.setPhoneNumber(sendCodeDto.getPhoneOrEmail());
            user.setResetNumber(code);
            SendSms.sendCodeToPhone(sendCodeDto.getPhoneOrEmail(), code);
        }
        repository.save(user);
        return true;
    }

    @Override
    public Boolean sendCodeForForgotPassword(SendCodeDto userSendCodeDto, String languageName) throws Exception {
        String code = GenerateCodeUtil.generateCode(4);
        if (userSendCodeDto.getPhoneOrEmail().contains("@")) {
            Optional<User> userOptional = repository.findByEmailAndHiddenFalse(userSendCodeDto.getPhoneOrEmail());
            if (userOptional.isEmpty()) {
                throw new Exception(EMAIL_IS_WRONG(languageName));
            } else if (userOptional.get().getPassword() == null) {
                throw new Exception(NOT_REGISTERED(languageName));
            }
            userOptional.get().setResetNumber(code);
            sendCodeToEmail(userSendCodeDto.getPhoneOrEmail(), code, languageName);
        } else {
            Optional<User> userOptional = repository.findByPhoneNumberAndHiddenFalse(userSendCodeDto.getPhoneOrEmail());
            if (userOptional.isEmpty()) {
                throw new Exception(PHONE_NUMBER_IS_WRONG(languageName));
            } else if (userOptional.get().getPassword() == null) {
                throw new Exception(NOT_REGISTERED(languageName));
            }
            userOptional.get().setResetNumber(code);
            SendSms.sendCodeToPhone(userSendCodeDto.getPhoneOrEmail(), code);
        }
        return true;
    }

    @Override
    public Boolean verifyCode(UserVerifyCodeDto userVerifyCodeDto, String languageName) throws Exception {
        Optional<User> optionalUser = repository.findByEmailAndHiddenFalse(userVerifyCodeDto.getPhoneOrEmail());
        Optional<User> optionalUserByPhone = repository.findByPhoneNumberAndHiddenFalse(userVerifyCodeDto.getPhoneOrEmail());
        if ((optionalUser.isPresent() && optionalUser.get().getResetNumber() != null) || (optionalUserByPhone.isPresent() && optionalUserByPhone.get().getResetNumber() != null)) {
            User user = optionalUser.orElseGet(optionalUserByPhone::get);
            if (!user.getResetNumber().equals(userVerifyCodeDto.getCode())) {
                throw new Exception(COD_IS_WRONG(languageName));
            }
            return true;
        }
        throw new EntityNotFoundException(User.class, "email or phone", String.valueOf(userVerifyCodeDto.getPhoneOrEmail()));
    }

    @Override
    public Boolean resetPassword(UserResetPasswordDto userResetPasswordDto, String languageName) throws Exception {
        Optional<User> optionalUser = userService.getByUsername(userResetPasswordDto.getPhoneOrEmail());
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(User.class, "email or phone", String.valueOf(userResetPasswordDto.getPhoneOrEmail()));
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getResetNumber().equals(userResetPasswordDto.getCode())) {
                throw new Exception(COD_IS_WRONG(languageName));
            }
            user.setPassword(passwordEncoder.encode(userResetPasswordDto.getPassword()));
            user.setResetNumber(null);
        }
        return true;
    }

    @Override
    public Guest createGuestAndSetToUserDevice(UserDevice device) throws Exception {
        Role role = roleRepository.findById(RoleConstants.GUEST_ID).orElseThrow(() -> new Exception("Guest Role not found"));
        Optional<UserDevice> optionalUserDevice = userDeviceService.findByDeviceId(device.getDeviceId());
        Optional<Guest> optionalGuest = guestRepository.findByEmail(device.getDeviceId());
        Guest savedGuest;
        if (optionalGuest.isEmpty()) {
            Guest guest = new Guest();
            guest.setFirstName("Guest");
            guest.setLastName("Guest");
            guest.setRole(role);
            guest.setEmail(device.getDeviceId());
            guest.setPassword(passwordEncoder.encode(device.getDeviceId()));
            savedGuest = repository.save(guest);
        } else {
            savedGuest = optionalGuest.get();
        }
        if (optionalUserDevice.isEmpty()) {
            throw new Exception("DeviceId is incorrect");
        }
        UserDevice userDevice = optionalUserDevice.get();
        userDeviceService.updateUserDevice(device, userDevice.getId());
        userDevice.setUser(savedGuest);
        return savedGuest;
    }

    private void sendCodeToEmail(String email, String code, String languageName) {
        SendCodeTextDto sendCodeTextDto = SEND_CODE_MESSAGE(languageName);
        SendEmail.sendCodeToEmail(email, sendCodeTextDto.getText() + code, sendCodeTextDto.getSubject());
    }
}