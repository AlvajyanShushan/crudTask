package egs.task.facade.book;

import egs.task.convert.BookConvert;
import egs.task.enums.BookStatus;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.book.BookPreviewDto;
import egs.task.models.dtos.book.BookSearchDto;
import egs.task.models.dtos.book.BookSpecification;
import egs.task.models.entities.Book;
import egs.task.models.entities.User;
import egs.task.repositories.AuthorRepository;
import egs.task.repositories.BookRepository;
import egs.task.utils.FindUser;
import egs.task.utils.RoleConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BookPreviewDtoFacade {
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final BookUtil bookUtil;
    @Lazy
    private final BookRepository bookRepository;
    @Lazy
    private final AuthorRepository authorRepository;

    public BookPreviewDtoFacade(FindUser findUser, BookUtil bookUtil, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.findUser = findUser;
        this.bookUtil = bookUtil;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookPreviewDto getBookById(Long id, Authentication authentication) throws Exception {
        User user = findUser.findUserByToken(authentication);
        Book book = bookUtil.getBook(id);
        if (user.getRole().getRoleName().equals(RoleConstants.USER_NAME)
                && !(book.getUser().getId().equals(user.getId()) || book.getBookStatus().equals(BookStatus.APPROVED))) {
            throw new Exception("You can not see non approved book was added by another user.");
        }
        return BookConvert.convertToBookPreviewDto(book, authorRepository);
    }

    public Page<BookPreviewDto> getAllBooks(Pageable pageable, BookSearchDto bookSearchDto, Authentication authentication) throws EntityNotFoundException {
        User user = findUser.findUserByToken(authentication);
        Page<Book> books = null;
        if (user.getRole().getRoleName().equals(RoleConstants.ADMIN_NAME)){
            books = bookRepository.findAll(BookSpecification.searchAdminBooks(bookSearchDto), pageable);
        }
        if (user.getRole().getRoleName().equals(RoleConstants.USER_NAME)){
            books = bookRepository.findAll(BookSpecification.searchUserBooks(bookSearchDto, user.getId()), pageable);
        }
        if (books != null) {
            return BookConvert.convertToPageBookPreviewDto(books,authorRepository);
        }
        return null;
    }
}
