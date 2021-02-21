package egs.task.repositories;

import egs.task.models.entities.AuthorBook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuthorBookRepository extends CommonRepository<AuthorBook> {
    @Modifying
    @Transactional
    @Query("DELETE FROM AuthorBook WHERE authorId in :authorIds")
    void deleteAllByAuthorIds(@Param("authorIds") List<Long> authorIds);
}
