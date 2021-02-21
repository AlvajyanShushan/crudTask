package egs.task.repositories;

import egs.task.models.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CommonRepository<Book> {
    @Query("SELECT COUNT(b) FROM Book b WHERE b.user.id=:userId AND b.hidden=false")
    int countAllUsersAddedBooks(@Param("userId") Long id);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.user.id=:userId AND b.hidden=false AND b.bookStatus=0")
    int countAllUsersAddedApprovedBooks(@Param("userId") Long id);
}
