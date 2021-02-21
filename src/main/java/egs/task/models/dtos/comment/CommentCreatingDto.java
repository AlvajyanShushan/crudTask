package egs.task.models.dtos.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentCreatingDto {
    @NotNull
    @Size(min = 2, message = "Comment should have at least 2 character.")
    private String text;
    @NotNull
    private Long bookId;
}