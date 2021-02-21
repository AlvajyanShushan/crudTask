package egs.task.facade.book;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.book.BookCreatingDto;
import egs.task.models.dtos.book.BookPreviewDto;
import egs.task.models.dtos.book.BookSearchDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookDtoFacadeBuilder {
    @Lazy
    private final BookCreatingDtoFacade bookCreatingDtoFacade;
    @Lazy
    private final BookPreviewDtoFacade bookPreviewDtoFacade;
    @Lazy
    private final BookDeletingDtoFacade bookDeletingDtoFacade;
    @Lazy
    private final BookUpdatingDtoFacade bookUpdatingDtoFacade;

    public BookDtoFacadeBuilder(BookCreatingDtoFacade bookCreatingDtoFacade,
                                BookPreviewDtoFacade bookPreviewDtoFacade,
                                BookDeletingDtoFacade bookDeletingDtoFacade, BookUpdatingDtoFacade bookUpdatingDtoFacade) {
        this.bookCreatingDtoFacade = bookCreatingDtoFacade;
        this.bookPreviewDtoFacade = bookPreviewDtoFacade;
        this.bookDeletingDtoFacade = bookDeletingDtoFacade;
        this.bookUpdatingDtoFacade = bookUpdatingDtoFacade;
    }

    public Long addBook(BookCreatingDto bookCreatingDto, Authentication authentication) throws EntityNotFoundException {
        return bookCreatingDtoFacade.addBook(bookCreatingDto, authentication);
    }

    public Boolean uploadImage(MultipartFile file, Long bookId, Authentication authentication) throws Exception {
        return bookCreatingDtoFacade.uploadImage(file, bookId, authentication);
    }

    public BookPreviewDto getBookById(Long id, Authentication authentication) throws Exception {
        return bookPreviewDtoFacade.getBookById(id, authentication);
    }

    public Boolean deleteImage(Long bookId, Authentication authentication) throws Exception {
        return bookDeletingDtoFacade.deleteImage(bookId, authentication);
    }

    public Boolean deleteBook(Long id, Authentication authentication) throws Exception {
        return bookDeletingDtoFacade.deleteBook(id, authentication);
    }

    public Boolean updateBook(BookCreatingDto bookCreatingDto, Authentication authentication, Long id) throws Exception {
        return bookUpdatingDtoFacade.updateBook(bookCreatingDto, authentication, id);
    }

    public Boolean approveOrNonApproveBook(Long bookId) throws EntityNotFoundException {
        return bookUpdatingDtoFacade.approveOrNonApproveBook(bookId);
    }

    public Page<BookPreviewDto> getAllBooks(Pageable pageable, BookSearchDto bookSearchDto, Authentication authentication) throws EntityNotFoundException {
        return bookPreviewDtoFacade.getAllBooks(pageable, bookSearchDto, authentication);
    }
}
