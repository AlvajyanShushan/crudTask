package egs.task.controllers;

import egs.task.configurations.securityConfig.TokenProvider;
import egs.task.enums.OsTypeEnum;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.user.UserFacadeBuilder;
import egs.task.models.ResponseModel;
import egs.task.models.dtos.AuthTokenDTO;
import egs.task.models.dtos.user.*;
import egs.task.models.entities.Guest;
import egs.task.models.entities.Role;
import egs.task.models.entities.User;
import egs.task.models.entities.UserDevice;
import egs.task.repositories.RoleRepository;
import egs.task.services.authentication.AuthenticationService;
import egs.task.services.user.UserService;
import egs.task.services.userDevice.UserDeviceService;
import egs.task.utils.DecodeTokenUtil;
import egs.task.utils.RoleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

import static egs.task.utils.AppConstants.USERNAME_OR_PASSWORD_IS_INCORRECT;

@RestController
@RequestMapping("auth/")
@Api(value = "authentication")
public class AuthenticationController extends BaseController {
    @Lazy
    private final AuthenticationManager authenticationManager;
    @Lazy
    private final TokenProvider jwtTokenUtil;
    @Lazy
    private final AuthenticationService authenticationService;
    @Lazy
    private final UserDeviceService userDeviceService;
    @Lazy
    private final UserService userService;
    @Lazy
    private final UserFacadeBuilder userFacadeBuilder;
    @Lazy
    private final RoleRepository roleRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil, AuthenticationService authenticationService,
                                    UserDeviceService userDeviceService, UserService userService, UserFacadeBuilder userFacadeBuilder, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationService = authenticationService;
        this.userDeviceService = userDeviceService;
        this.userService = userService;
        this.userFacadeBuilder = userFacadeBuilder;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("signIn")
    public ResponseModel login(@Valid @RequestBody LoginUserDto loginUser,
                               @RequestHeader(value = "DeviceId", required = false) String deviceId,
                               @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader(value = "LanguageName", defaultValue = "hy") String languageName) {
        try {
            Optional<User> optionalUser = userService.getByUsername(loginUser.getEmailOrPhone());
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getEmailOrPhone(), loginUser.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            final String roleName = DecodeTokenUtil.getRoleFromToken(token);
            Optional<UserDevice> optionalDevice = userDeviceService.findByDeviceId(deviceId);
            optionalDevice.ifPresent(userDevice -> {
                userDevice.setUser(optionalUser.get());
                userDevice.setLanguageName(languageName);
                try {
                    userDeviceService.save(userDevice);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            });
            AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                    .role(roleName)
                    .token(token)
                    .build();
            return ResponseModel.builder()
                    .data(authTokenDTO)
                    .success(true)
                    .message("OK")
                    .build();
        }  catch (Exception e) {
            return ResponseModel.builder()
                    .success(false)
                    .message(USERNAME_OR_PASSWORD_IS_INCORRECT(languageName))
                    .build();
        }
    }

    @PostMapping("signUp")
    @PreAuthorize("hasAnyRole('GUEST')")
    public ResponseModel registration(@Valid @RequestBody UserCreatingDto userCreatingDto, @RequestHeader("DeviceId") String deviceId,
                                      @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader("LanguageName") String languageName) {
        try {
            userFacadeBuilder.registration(userCreatingDto, deviceId);
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCreatingDto.getEmail(), userCreatingDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            final String roleName = DecodeTokenUtil.getRoleFromToken(token);
            AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                    .role(roleName)
                    .token(token)
                    .build();
            userService.deleteByUsername(deviceId);
            return ResponseModel.builder().data(authTokenDTO)
                    .success(true)
                    .message("OK")
                    .build();
        } catch (AuthenticationException e) {
            return ResponseModel.builder().success(false).message(USERNAME_OR_PASSWORD_IS_INCORRECT(languageName)).build();
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @PutMapping("sendCodeForSignUp")
    @PreAuthorize("hasAnyRole('GUEST')")
    public ResponseModel sendCodeForSignUp(@RequestBody @Valid SendCodeDto sendCodeDto,
                                           @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader("LanguageName") String languageName) {
        try {
            return createResult(authenticationService.sendCodeForSignUp(sendCodeDto, languageName), "Code was sent successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @PutMapping("sendCodeForForgotPassword")
    public ResponseModel sendCodeForForgotPassword(@RequestBody @Valid SendCodeDto sendCodeDto,
                                                   @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader("LanguageName") String languageName) {
        try {
            return createResult(authenticationService.sendCodeForForgotPassword(sendCodeDto, languageName), "Code was sent successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @PutMapping("resetPassword")
    public ResponseModel resetPassword(@Valid @RequestBody UserResetPasswordDto userResetPasswordDto,
                                       @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader("LanguageName") String languageName) {
        try {
            return createResult(authenticationService.resetPassword(userResetPasswordDto, languageName), "Password was successfully changed.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @PutMapping("verifyCode")
    public ResponseModel verifyCode(@RequestBody @Valid UserVerifyCodeDto userVerifyCodeDto,
                                    @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader("LanguageName") String languageName) {
        try {
            return createResult(authenticationService.verifyCode(userVerifyCodeDto, languageName), "Code was successfully checked.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @Transactional
    @PutMapping("logout")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN', 'USER', 'GUEST', 'DRIVER')")
    public ResponseModel logout(@RequestHeader("deviceId") String deviceId) {
        try {
            Optional<UserDevice> optionalUserDevice = userDeviceService.findByDeviceId(deviceId);
            if (optionalUserDevice.isEmpty()) {
                throw new Exception("DeviceId is incorrect");
            }
            Guest guest = authenticationService.createGuestAndSetToUserDevice(optionalUserDevice.get());
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(guest.getEmail(), deviceId));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            final String roleName = DecodeTokenUtil.getRoleFromToken(token);
            AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                    .role(roleName)
                    .token(token)
                    .build();
            return createResult(authTokenDTO, "Ok");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @Transactional
    @ApiOperation(value = "Login API for guest")
    @PostMapping("signUpAsGuest")
    public ResponseModel signUpAsGuest(@RequestHeader("DeviceId") String deviceId,
                                       @RequestHeader("DeviceToken") String deviceToken,
                                       @RequestHeader("Model") String modelName,
                                       @RequestHeader("osVersion") String osVersion,
                                       @RequestHeader("appVersion") String appVersion,
                                       @ApiParam("OsTypeId must be '1' for Android,   '2' for Ios and '3' for Web ") @RequestHeader("OsTypeId") int osTypeId,
                                       @ApiParam("LanguageName must be 'en' or 'ru' or 'hy'") @RequestHeader(value = "LanguageName") String languageName) {
        try {
            Role role = roleRepository.findById(RoleConstants.GUEST_ID).orElseThrow(() -> new Exception("Guest Role not found"));
            Optional<UserDevice> optionalUserDevice = userDeviceService.findByDeviceId(deviceId);
            Guest guest = new Guest();
            guest.setFirstName("Guest");
            guest.setLastName("Guest");
            guest.setRole(role);
            guest.setEmail(deviceId);
            guest.setPassword(passwordEncoder.encode(deviceId));
            if (languageName == null || (!languageName.equalsIgnoreCase("en") && !languageName.equalsIgnoreCase("hy") && !languageName.equalsIgnoreCase("ru"))) {
                throw new Exception("LanguageName must be 'hy' or 'en' or 'ru'.");
            }
            UserDevice device = UserDevice.builder()
                    .deviceId(deviceId)
                    .deviceToken(deviceToken)
                    .modelName(modelName)
                    .osVersion(osVersion)
                    .appVersion(appVersion)
                    .languageName(languageName)
                    .user(guest)
                    .osTypeEnum(OsTypeEnum.valueOf(osTypeId).orElseThrow(() -> new Exception("os Type not found")))
                    .build();
            if (optionalUserDevice.isPresent()) {
                authenticationService.createGuestAndSetToUserDevice(device);
            } else {
                if (userService.getByUsername(deviceId).isPresent())
                    userService.deleteUser(userService.getByUsername(deviceId).get());
                userService.save(guest);
                userDeviceService.save(device);
            }
            return login(new LoginUserDto(deviceId, deviceId), deviceId, languageName);
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

}