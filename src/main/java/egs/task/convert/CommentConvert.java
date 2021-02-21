package egs.task.convert;

import egs.task.models.dtos.comment.CommentPreviewDto;
import egs.task.models.dtos.user.UserInBookListPreviewDto;
import egs.task.models.entities.Comment;
import egs.task.models.entities.User;
import egs.task.repositories.UserRepository;
import org.springframework.data.domain.Page;

import static egs.task.utils.Configuration.userPath;

public class CommentConvert {
    public static CommentPreviewDto convertToPreviewDto(Comment comment, UserRepository userRepository) {
        User user = userRepository.findUserByCommentId(comment.getId());
        UserInBookListPreviewDto userInBookListPreviewDto = new UserInBookListPreviewDto();
        userInBookListPreviewDto.setId(user.getId());
        userInBookListPreviewDto.setFirstName(user.getFirstName());
        userInBookListPreviewDto.setLastName(user.getLastName());
        userInBookListPreviewDto.setImageUrl(user.getImageUrl() != null ? userPath + user.getImageUrl() : null);
        return CommentPreviewDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .user(userInBookListPreviewDto)
                .build();
    }

    public static Page<CommentPreviewDto> convertPageToPreviewDto(Page<Comment> comments, UserRepository userRepository) {
        return comments.map(comment -> CommentConvert.convertToPreviewDto(comment, userRepository));
    }
}
