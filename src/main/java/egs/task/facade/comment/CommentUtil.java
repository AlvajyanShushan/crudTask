package egs.task.facade.comment;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.entities.Comment;
import egs.task.repositories.CommentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentUtil {
    @Lazy
    private final CommentRepository commentRepository;

    public CommentUtil(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment getComment(Long id) throws EntityNotFoundException {
       Optional<Comment> commentOptional = commentRepository.findById(id);
       if (commentOptional.isEmpty()) {
           throw new EntityNotFoundException(Comment.class, " commentId", String.valueOf(id));
       }
       return commentOptional.get();
   }
}
