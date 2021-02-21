package egs.task.models.dtos.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatingDto {
    @NotNull
    private String emailOrPhone;
    @Size(min = 3, max = 30, message = "First name must be in range 3-30")
    private String firstName;
    @Size(min = 3, max = 30, message = "Last name must be in range 3-30")
    private String lastName;
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long.")
    private String password;
}
