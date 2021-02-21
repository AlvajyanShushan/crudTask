package egs.task.repositories;

import egs.task.models.entities.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CommonRepository<Author> {
    @Query("SELECT a FROM Author a JOIN AuthorBook ab ON a.id=ab.authorId WHERE ab.bookId =:bookId")
    List<Author> findAuthorsByBookId(@Param("bookId") Long id);
}