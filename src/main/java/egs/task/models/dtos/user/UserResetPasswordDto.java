package egs.task.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserResetPasswordDto {

    @NotNull
    private String phoneOrEmail;
    @NotNull
    private String code;

    @NotNull
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long")
    private String password;
}

