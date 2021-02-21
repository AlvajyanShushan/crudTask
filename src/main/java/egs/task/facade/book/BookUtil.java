package egs.task.facade.book;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.entities.Book;
import egs.task.repositories.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookUtil {
    @Lazy
    private final BookRepository bookRepository;

    public BookUtil(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBook(Long id) throws EntityNotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty() || optionalBook.get().getHidden()) {
            throw new EntityNotFoundException(Book.class, " bookId", String.valueOf(id));
        }
        return optionalBook.get();
    }
}
