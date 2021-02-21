package egs.task.convert;

import egs.task.enums.BookStatus;
import egs.task.models.dtos.book.BookCreatingDto;
import egs.task.models.dtos.book.BookPreviewDto;
import egs.task.models.entities.Book;
import egs.task.repositories.AuthorRepository;
import org.springframework.data.domain.Page;

import static egs.task.utils.Configuration.bookPath;

public class BookConvert {
    public static Book convertToEntity(BookCreatingDto bookCreatingDto) {
        return Book.builder()
                .title(bookCreatingDto.getTitle())
                .description(bookCreatingDto.getDescription())
                .pageCount(bookCreatingDto.getPageCount())
                .publishedDate(bookCreatingDto.getPublishedDate())
                .bookStatus(BookStatus.NON_APPROVED)
                .build();
    }

    public static BookPreviewDto convertToBookPreviewDto(Book book, AuthorRepository authorRepository) {
        return BookPreviewDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .coverPhoto(book.getCoverPhoto() != null ? bookPath + book.getCoverPhoto() : null)
                .pageCount(book.getPageCount())
                .publishedDate(book.getPublishedDate())
                .authorDtoList(AuthorConvert.convertToDtoList(authorRepository.findAuthorsByBookId(book.getId())))
                .bookStatusValue(book.getBookStatus().getValue())
                .build();
    }

    public static Page<BookPreviewDto> convertToPageBookPreviewDto(Page<Book> books, AuthorRepository authorRepository) {
        return books.map(book -> BookConvert.convertToBookPreviewDto(book, authorRepository));
    }
}
