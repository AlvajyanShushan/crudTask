package egs.task.facade.book;

import egs.task.convert.AuthorConvert;
import egs.task.enums.BookStatus;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.AbstractEntity;
import egs.task.models.dtos.book.BookCreatingDto;
import egs.task.models.entities.Author;
import egs.task.models.entities.Book;
import egs.task.models.entities.User;
import egs.task.repositories.AuthorBookRepository;
import egs.task.repositories.AuthorRepository;
import egs.task.utils.FindUser;
import egs.task.utils.RoleConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookUpdatingDtoFacade {
    @Lazy
    private final BookUtil bookUtil;
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final AuthorRepository authorRepository;
    @Lazy
    private final AuthorBookRepository authorBookRepository;

    public BookUpdatingDtoFacade(BookUtil bookUtil, FindUser findUser,
                                 AuthorRepository authorRepository, AuthorBookRepository authorBookRepository) {
        this.bookUtil = bookUtil;
        this.findUser = findUser;
        this.authorRepository = authorRepository;
        this.authorBookRepository = authorBookRepository;
    }

    public Boolean approveOrNonApproveBook(Long bookId) throws EntityNotFoundException {
        Book book = bookUtil.getBook(bookId);
        if (book.getBookStatus().equals(BookStatus.APPROVED)) {
            book.setBookStatus(BookStatus.NON_APPROVED);
        } else {
            book.setBookStatus(BookStatus.APPROVED);
        }
        return true;
    }

    public Boolean updateBook(BookCreatingDto bookCreatingDto, Authentication authentication, Long id) throws Exception {
        Book book = bookUtil.getBook(id);
        User user = findUser.findUserByToken(authentication);
        if (user.getRole().getRoleName().equals(RoleConstants.USER_NAME) && !book.getUser().getId().equals(user.getId())) {
            throw new Exception("You do not have permission.");
        }
        List<Long> authorIds =
                book.getAuthors().stream().map(AbstractEntity::getId).collect(Collectors.toList());
        authorBookRepository.deleteAllByAuthorIds(authorIds);
        authorIds.forEach(authorRepository::deleteById);
        book.setTitle(bookCreatingDto.getTitle());
        book.setDescription(bookCreatingDto.getDescription());
        book.setPageCount(bookCreatingDto.getPageCount());
        book.setPublishedDate(bookCreatingDto.getPublishedDate());
        List<Author> authors = AuthorConvert.convertToEntityList(bookCreatingDto.getAuthorDtoList());
        authorRepository.saveAll(authors);
        book.setAuthors(authors);
        return true;
    }
}
