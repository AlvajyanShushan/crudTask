package egs.task.models.dtos.user;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdatingDto {
//    @NotNull
//    private String email;
    @Size(min = 3, max = 30, message = "First name must be in range 3-30")
    private String firstName;
    @Size(min = 3, max = 30, message = "Last name must be in range 3-30")
    private String lastName;
//    @Size(min = 12, max = 12, message = "Phone number must have 12 number")
//    private String phoneNumber;
    private Long dateOfBirth;
}
