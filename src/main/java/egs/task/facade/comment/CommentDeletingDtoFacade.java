package egs.task.facade.comment;

import egs.task.models.entities.Comment;
import egs.task.models.entities.User;
import egs.task.repositories.CommentRepository;
import egs.task.utils.FindUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentDeletingDtoFacade {
    @Lazy
    private final CommentUtil commentUtil;
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final CommentRepository commentRepository;

    public CommentDeletingDtoFacade(CommentUtil commentUtil, FindUser findUser, CommentRepository commentRepository) {
        this.commentUtil = commentUtil;
        this.findUser = findUser;
        this.commentRepository = commentRepository;
    }

    public Boolean deleteComment(Long id, Authentication authentication) throws Exception {
        Comment comment = commentUtil.getComment(id);
        User user = findUser.findUserByToken(authentication);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new Exception("You do not have permission.");
        }
        commentRepository.delete(comment);
        return true;
    }
}
