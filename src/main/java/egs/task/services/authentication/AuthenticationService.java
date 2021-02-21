package egs.task.services.authentication;


import egs.task.models.dtos.user.SendCodeDto;
import egs.task.models.dtos.user.UserResetPasswordDto;
import egs.task.models.dtos.user.UserVerifyCodeDto;
import egs.task.models.entities.Guest;
import egs.task.models.entities.User;
import egs.task.models.entities.UserDevice;
import egs.task.services.CommonService;

public interface AuthenticationService extends CommonService<User> {

    Boolean sendCodeForSignUp(SendCodeDto sendCodeDto, String languageName) throws Exception;

    Boolean sendCodeForForgotPassword(SendCodeDto sendCodeDto, String languageName) throws Exception;

    Boolean verifyCode(UserVerifyCodeDto userVerifyCodeDto, String languageName) throws Exception;

    Boolean resetPassword(UserResetPasswordDto userResetPasswordDto, String languageName) throws Exception;

    Guest createGuestAndSetToUserDevice(UserDevice device) throws Exception;
}
