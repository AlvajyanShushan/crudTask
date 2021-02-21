package egs.task.facade.comment;

import egs.task.convert.CommentConvert;
import egs.task.enums.BookStatus;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.book.BookUtil;
import egs.task.models.dtos.comment.CommentPreviewDto;
import egs.task.models.entities.Book;
import egs.task.models.entities.Comment;
import egs.task.repositories.CommentRepository;
import egs.task.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public CommentPreviewDtoFacade(CommentUtil commentUtil, UserRepository userRepository,
                                   CommentRepository commentRepository, BookUtil bookUtil) {
        this.commentUtil = commentUtil;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookUtil = bookUtil;
    }

    public CommentPreviewDto getCommentById(Long id) throws EntityNotFoundException {
        Comment comment = commentUtil.getComment(id);
        return CommentConvert.convertToPreviewDto(comment, userRepository);
    }

    public Page<CommentPreviewDto> getAllCommentsByBookId(Pageable pageable, Long bookId) throws EntityNotFoundException {
        Book book = bookUtil.getBook(bookId);
        if (book.getBookStatus().equals(BookStatus.APPROVED)) {
            Page<Comment> comments = commentRepository.findAllByBookId(bookId, pageable);
            return CommentConvert.convertPageToPreviewDto(comments, userRepository);
        }
        return null;
    }
}
