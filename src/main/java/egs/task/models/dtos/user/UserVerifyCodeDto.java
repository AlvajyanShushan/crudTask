package egs.task.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserVerifyCodeDto {
    @NotNull
    private String phoneOrEmail;

    @NotNull
    private String code;
}
