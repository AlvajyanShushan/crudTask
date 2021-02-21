package egs.task.controllers;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.comment.CommentDtoFacadeBuilder;
import egs.task.models.ResponseModel;
import egs.task.models.dtos.comment.CommentCreatingDto;
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

import javax.validation.Valid;

@RestController
@RequestMapping("comment/")
@Api(value = "comment")
public class CommentController extends BaseController {
    @Lazy
    private final CommentDtoFacadeBuilder facadeBuilder;

    @Autowired
    CommentController(CommentDtoFacadeBuilder facadeBuilder) {
        this.facadeBuilder = facadeBuilder;
    }

    @ApiOperation(value = "API for user to add comment to book.")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseModel addComment(Authentication authentication,
                                    @RequestBody @Valid CommentCreatingDto commentCreatingDto) {
        try {
            return createResult(facadeBuilder.addComment(commentCreatingDto, authentication), "The comment was added successfully");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for  user to get comment by id.")
    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseModel getById(@PathVariable("id") Long id) {
        try {
            return createResult(facadeBuilder.getCommentById(id), "The comment was retrieved successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for user to update comment.")
    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseModel update(@Valid @RequestBody CommentCreatingDto commentCreatingDto, Authentication authentication,
                                @ApiParam(required = true) @PathVariable(value = "id") Long id) {
        try {
            return createResult(facadeBuilder.updateComment(commentCreatingDto, authentication, id), "The comment was edited successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for user to delete comment.")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseModel deleteComment(@ApiParam(required = true) @PathVariable(value = "id") Long id, Authentication authentication) {
        try {
            return createResult(facadeBuilder.deleteComment(id, authentication), "The book is deleted successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    @ApiOperation(value = "API for user to get all comments by bookId")
    @PostMapping("getAllCommentsByBookId/{page}/{size}")
    @PreAuthorize("hasRole('USER')")
    public ResponseModel getAllCommentsByBookId(@PathVariable("page") int page, @PathVariable("size") int size,
                                                @RequestParam Long bookId, Authentication authentication) {
        try {
            return createResult(facadeBuilder.getAllCommentsByBookId(PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"),
                    bookId, authentication), "List of comments were retrieved successfully.");
        } catch (EntityNotFoundException e) {
            return createErrorResult(e);
        }
    }
}
