package egs.task.facade.comment;

import egs.task.enums.BookStatus;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.book.BookUtil;
import egs.task.models.dtos.comment.CommentCreatingDto;
import egs.task.models.entities.Book;
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
public class CommentCreatingDtoFacade {
    @Lazy
    private final BookUtil bookUtil;
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final CommentRepository commentRepository;

    public CommentCreatingDtoFacade(BookUtil bookUtil, FindUser findUser, CommentRepository commentRepository) {
        this.bookUtil = bookUtil;
        this.findUser = findUser;
        this.commentRepository = commentRepository;
    }

    public Long addComment(CommentCreatingDto commentCreatingDto, Authentication authentication) throws Exception {
        Book book = bookUtil.getBook(commentCreatingDto.getBookId());
        User user = findUser.findUserByToken(authentication);
        if (book.getBookStatus().equals(BookStatus.APPROVED) || book.getUser().getId().equals(user.getId())) {
            Comment comment = new Comment();
            comment.setBook(book);
            comment.setText(commentCreatingDto.getText());
            comment.setUser(user);
            return commentRepository.save(comment).getId();
        }
        throw new Exception("The book is non approved or you have not permission.");
    }
}
