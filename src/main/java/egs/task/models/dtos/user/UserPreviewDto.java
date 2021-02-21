package egs.task.models.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreviewDto {
    private Long userId;
    private String firsName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String imageUrl;
    private Long dateOfBirth;
    private int userStatusValue;
    private boolean enablePushNotification;
    private boolean enableEmailNotification;
}