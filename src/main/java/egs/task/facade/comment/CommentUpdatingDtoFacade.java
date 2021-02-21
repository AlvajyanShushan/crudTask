package egs.task.facade.comment;

import egs.task.enums.BookStatus;
import egs.task.facade.book.BookUtil;
import egs.task.models.dtos.comment.CommentCreatingDto;
import egs.task.models.entities.Book;
import egs.task.models.entities.Comment;
import egs.task.models.entities.User;
import egs.task.utils.FindUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentUpdatingDtoFacade {
    @Lazy
    private final CommentUtil commentUtil;
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final BookUtil bookUtil;

    public CommentUpdatingDtoFacade(CommentUtil commentUtil, FindUser findUser, BookUtil bookUtil) {
        this.commentUtil = commentUtil;
        this.findUser = findUser;
        this.bookUtil = bookUtil;
    }

    public Boolean updateComment(CommentCreatingDto commentCreatingDto, Authentication authentication, Long id) throws Exception {
        Comment comment = commentUtil.getComment(id);
        User user = findUser.findUserByToken(authentication);
        Book book = bookUtil.getBook(commentCreatingDto.getBookId());
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new Exception("You do not have permission.");
        }
        comment.setText(commentCreatingDto.getText());
        if (book.getBookStatus().equals(BookStatus.APPROVED) || book.getUser().getId().equals(user.getId())) {
            comment.setBook(book);
        }
        return true;
    }
}
