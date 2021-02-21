package egs.task.facade.comment;

import egs.task.convert.CommentConvert;
import egs.task.enums.BookStatus;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.book.BookUtil;
import egs.task.models.dtos.comment.CommentPreviewDto;
import egs.task.models.entities.Book;
import egs.task.models.entities.Comment;
import egs.task.models.entities.User;
import egs.task.repositories.CommentRepository;
import egs.task.repositories.UserRepository;
import egs.task.utils.FindUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentPreviewDtoFacade {
    @Lazy
    private final CommentUtil commentUtil;
    @Lazy
    private final UserRepository userRepository;
    @Lazy
    private final CommentRepository commentRepository;
    @Lazy
    private final BookUtil bookUtil;
    @Lazy
    private final FindUser findUser;

    public CommentPreviewDtoFacade(CommentUtil commentUtil, UserRepository userRepository,
                                   CommentRepository commentRepository, BookUtil bookUtil, FindUser findUser) {
        this.commentUtil = commentUtil;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookUtil = bookUtil;
        this.findUser = findUser;
    }

    public CommentPreviewDto getCommentById(Long id) throws EntityNotFoundException {
        Comment comment = commentUtil.getComment(id);
        return CommentConvert.convertToPreviewDto(comment, userRepository);
    }

    public Page<CommentPreviewDto> getAllCommentsByBookId(Pageable pageable, Long bookId, Authentication authentication)
            throws EntityNotFoundException {
        User user = findUser.findUserByToken(authentication);
        Book book = bookUtil.getBook(bookId);
        if (book.getBookStatus().equals(BookStatus.APPROVED) || book.getUser().equals(user)) {
            Page<Comment> comments = commentRepository.findAllByBookId(bookId, pageable);
            return CommentConvert.convertPageToPreviewDto(comments, userRepository);
        }
        return null;
    }
}
