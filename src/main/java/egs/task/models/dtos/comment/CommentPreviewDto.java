package egs.task.models.dtos.comment;

import egs.task.models.dtos.user.UserInBookListPreviewDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPreviewDto {
    private Long id;
    private String text;
    private UserInBookListPreviewDto user;
}