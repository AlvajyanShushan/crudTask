package egs.task.models.dtos.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListPreviewDto {
    private Long userId;
    private String firsName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String imageUrl;
    private int addedBooksCount;
    private int approvedBooksCount;
}