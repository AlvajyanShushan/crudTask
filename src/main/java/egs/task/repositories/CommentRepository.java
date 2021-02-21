package egs.task.repositories;

import egs.task.models.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CommonRepository<Comment>{

    Page<Comment> findAllByBookId(Long bookId, Pageable pageable);
}
