package egs.task.facade.comment;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.comment.CommentCreatingDto;
import egs.task.models.dtos.comment.CommentPreviewDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CommentDtoFacadeBuilder {
    @Lazy
    private final CommentCreatingDtoFacade commentCreatingDtoFacade;
    @Lazy
    private final CommentPreviewDtoFacade commentPreviewDtoFacade;
    @Lazy
    private final CommentUpdatingDtoFacade commentUpdatingDtoFacade;
    @Lazy
    private final CommentDeletingDtoFacade commentDeletingDtoFacade;

    public CommentDtoFacadeBuilder(CommentCreatingDtoFacade commentCreatingDtoFacade,
                                   CommentPreviewDtoFacade commentPreviewDtoFacade,
                                   CommentUpdatingDtoFacade commentUpdatingDtoFacade,
                                   CommentDeletingDtoFacade commentDeletingDtoFacade) {
        this.commentCreatingDtoFacade = commentCreatingDtoFacade;
        this.commentPreviewDtoFacade = commentPreviewDtoFacade;
        this.commentUpdatingDtoFacade = commentUpdatingDtoFacade;
        this.commentDeletingDtoFacade = commentDeletingDtoFacade;
    }

    public Long addComment(CommentCreatingDto commentCreatingDto, Authentication authentication) throws Exception {
        return commentCreatingDtoFacade.addComment(commentCreatingDto, authentication);
    }

    public CommentPreviewDto getCommentById(Long id) throws EntityNotFoundException {
        return commentPreviewDtoFacade.getCommentById(id);
    }

    public Boolean updateComment(CommentCreatingDto commentCreatingDto, Authentication authentication, Long id) throws Exception {
        return commentUpdatingDtoFacade.updateComment(commentCreatingDto, authentication, id);
    }

    public Boolean deleteComment(Long id, Authentication authentication) throws Exception {
        return commentDeletingDtoFacade.deleteComment(id, authentication);
    }

    public Page<CommentPreviewDto> getAllCommentsByBookId(Pageable pageable, Long bookId) throws EntityNotFoundException {
        return commentPreviewDtoFacade.getAllCommentsByBookId(pageable, bookId);
    }
}
