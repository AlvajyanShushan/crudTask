package egs.task.controllers;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.book.BookDtoFacadeBuilder;
import egs.task.models.ResponseModel;
import egs.task.models.dtos.book.BookCreatingDto;
import egs.task.models.dtos.book.BookSearchDto;
import egs.task.utils.Configuration;
import egs.task.utils.FileUtilForTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static egs.task.utils.AppConstants.ADD_BOOK;

@RestController
@RequestMapping("book/")
@Api(value = "book")
public class BookController extends BaseController {
    @Lazy
    private final BookDtoFacadeBuilder facadeBuilder;

    @Autowired
    BookController(BookDtoFacadeBuilder facadeBuilder) {
        this.facadeBuilder = facadeBuilder;
    }

    @ApiOperation(value = "API for admin and user to add book.")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseModel addBook(@RequestHeader("languageName") String languageName, Authentication authentication,
                                 @RequestBody @Valid BookCreatingDto bookCreatingDto) {
        try {
            return createResult(facadeBuilder.addBook(bookCreatingDto, authentication), ADD_BOOK(languageName));
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "Api for admin and user to upload book's image.")
    @PostMapping("uploadImage")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseModel uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("bookId") Long bookId,
                                     Authentication authentication) {
        try {
            return createResult(facadeBuilder.uploadImage(file, bookId, authentication), "Book's image was uploaded successfully");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for Admin and user to get book by id.")
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseModel getById(@PathVariable("id") Long id, Authentication authentication) {
        try {
            return createResult(facadeBuilder.getBookById(id, authentication), "Book was retrieved successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for admin and user to update book.")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseModel update(@Valid @RequestBody BookCreatingDto bookCreatingDto, Authentication authentication,
                                @ApiParam(required = true) @PathVariable(value = "id") Long id) {
        try {
            return createResult(facadeBuilder.updateBook(bookCreatingDto, authentication, id), "Book's data were edited successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @GetMapping("getImage/{filename:.+}")
    public byte[] serveFile(@PathVariable String filename) throws IOException {
        return FileUtilForTask.loadFile(filename, Configuration.BOOK_IMAGE_DIR_IN_PROJECT, -1, -1);
    }

    @ApiOperation(value = "API for admin to approve the book or not.")
    @PutMapping("approveOrNonApproveBook")
    @PreAuthorize("hasRole( 'ADMIN')")
    public ResponseModel approveOrNonApproveBook(@RequestParam(value = "bookId") Long bookId) {
        try {
            return createResult(facadeBuilder.approveOrNonApproveBook(bookId), "The status of the book was changed successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for admin and user to delete  book.")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole( 'ADMIN', 'USER')")
    public ResponseModel deleteBook(@ApiParam(required = true) @PathVariable(value = "id") Long id, Authentication authentication) {
        try {
            return createResult(facadeBuilder.deleteBook(id, authentication), "The book is deleted successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for admin and user to delete book's cover photo.")
    @DeleteMapping("deleteImage")
    @PreAuthorize("hasAnyRole( 'ADMIN', 'USER')")
    public ResponseModel deleteImage(@RequestParam("bookId") Long bookId, Authentication authentication) {
        try {
            return createResult(facadeBuilder.deleteImage(bookId, authentication), "The cover photo of the book is deleted.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for admin and user to get all books")
    @PostMapping("getAllBooks/{page}/{size}")
    @PreAuthorize("hasAnyRole( 'ADMIN', 'USER')")
    public ResponseModel getAllBooks(@PathVariable("page") int page, @PathVariable("size") int size,
                                     @RequestBody BookSearchDto bookSearchDto,
                                     Authentication authentication) {
        try {
            return createResult(facadeBuilder.getAllBooks(PageRequest.of(page, size, Sort.Direction.ASC, "title"),
                    bookSearchDto, authentication),
                    "List of books retrieved successfully.");
        } catch (EntityNotFoundException e) {
            return createErrorResult(e);
        }
    }
}
