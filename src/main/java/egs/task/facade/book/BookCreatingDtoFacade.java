package egs.task.facade.book;

import egs.task.convert.AuthorConvert;
import egs.task.convert.BookConvert;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.book.BookCreatingDto;
import egs.task.models.entities.Author;
import egs.task.models.entities.Book;
import egs.task.models.entities.User;
import egs.task.repositories.AuthorRepository;
import egs.task.repositories.BookRepository;
import egs.task.utils.Configuration;
import egs.task.utils.FileUtilForTask;
import egs.task.utils.FindUser;
import egs.task.utils.RoleConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookCreatingDtoFacade {
    @Lazy
    private final BookRepository bookRepository;
    @Lazy
    private final FindUser findUser;
    @Lazy
    private final AuthorRepository authorRepository;

    public BookCreatingDtoFacade(BookRepository bookRepository, FindUser findUser,
                                 AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.findUser = findUser;
        this.authorRepository = authorRepository;
    }

    public Long addBook(BookCreatingDto bookCreatingDto, Authentication authentication) throws EntityNotFoundException {
        User user = findUser.findUserByToken(authentication);
        Book book = BookConvert.convertToEntity(bookCreatingDto);
        List<Author> authors = AuthorConvert.convertToEntityList(bookCreatingDto.getAuthorDtoList());
        authorRepository.saveAll(authors);
        book.setAuthors(authors);
        book.setUser(user);
        return bookRepository.save(book).getId();
    }

    public Boolean uploadImage(MultipartFile file, Long bookId, Authentication authentication) throws Exception {
        User user = findUser.findUserByToken(authentication);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new EntityNotFoundException(Book.class, " bookId", String.valueOf(bookId));
        }
        Book book = optionalBook.get();
        if (user.getRole().getRoleName().equals(RoleConstants.USER_NAME)
                && !book.getUser().getId().equals(user.getId())) {
            throw new Exception("You no have any permission to upload image to book added by another users.");
        }
        final String filename = FileUtilForTask.saveFile(Configuration.BOOK_IMAGE_DIR_IN_PROJECT, file);
        book.setCoverPhoto(filename);
        return true;
    }
}
