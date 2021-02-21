package egs.task.facade.user;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.user.UserCreatingDto;
import egs.task.models.entities.Role;
import egs.task.models.entities.User;
import egs.task.models.entities.UserDevice;
import egs.task.repositories.RoleRepository;
import egs.task.repositories.UserRepository;
import egs.task.services.userDevice.UserDeviceService;
import egs.task.utils.Configuration;
import egs.task.utils.FileUtilForTask;
import egs.task.utils.FindUser;
import egs.task.utils.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserCreatingDtoFacade {
    @Lazy
    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final UserDeviceService userDeviceService;
    @Lazy
    private final FindUser findUser;

    @Autowired
    public UserCreatingDtoFacade(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository, UserDeviceService userDeviceService, FindUser findUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDeviceService = userDeviceService;
        this.findUser = findUser;
    }

    public Long registration(UserCreatingDto userCreatingDto, String deviceId) throws EntityNotFoundException {
        Optional<User> userOptional = userRepository.findByEmailAndHiddenFalse(userCreatingDto.getEmailOrPhone());
        Optional<User> userOptionalByPhone = userRepository.findByPhoneNumberAndHiddenFalse(userCreatingDto.getEmailOrPhone());
        if (userOptional.isEmpty() && userOptionalByPhone.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        } else if (userOptional.isPresent() && userOptional.get().getFirstName() != null && (userOptional.get().getEmail() != null || userOptional.get().getPhoneNumber() != null)) {
            throw new IllegalArgumentException("An account with this Email already exists.");
        } else if (userOptionalByPhone.isPresent() && userOptionalByPhone.get().getFirstName() != null && (userOptionalByPhone.get().getEmail() != null || userOptionalByPhone.get().getPhoneNumber() != null)) {
            throw new IllegalArgumentException("An account with this phone number already exists.");
        }
        User user = userOptional.orElseGet(userOptionalByPhone::get);
        user.setFirstName(userCreatingDto.getFirstName());
        user.setLastName(userCreatingDto.getLastName());
        user.setPassword(passwordEncoder.encode(userCreatingDto.getPassword()));
        user.setRole(roleRepository.findById(RoleConstants.USER_ID).orElseThrow(()
                -> new EntityNotFoundException(Role.class, "Role ", String.valueOf(RoleConstants.USER_ID))));
        User save = userRepository.save(user);
        Optional<UserDevice> userDevice = userDeviceService.findByDeviceId(deviceId);
        userDevice.get().setUser(save);
        return save.getId();
    }

    public Boolean uploadImage(MultipartFile file, Authentication authentication) throws EntityNotFoundException {
        User user = findUser.findUserByToken(authentication);
        String filename = FileUtilForTask.saveFile(Configuration.USER_IMAGE_DIR_IN_PROJECT, file);
        user.setImageUrl(filename);
        return true;
    }
}