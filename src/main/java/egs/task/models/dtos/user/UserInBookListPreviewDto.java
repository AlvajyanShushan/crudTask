package egs.task.models.dtos.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInBookListPreviewDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String imageUrl;
}
