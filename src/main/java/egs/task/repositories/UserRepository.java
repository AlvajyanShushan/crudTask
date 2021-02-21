package egs.task.repositories;

import egs.task.models.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email=:username AND u.hidden=false")
    Optional<User> findByEmailAndHiddenFalse(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.phoneNumber=:username AND u.hidden=false")
    Optional<User> findByPhoneNumberAndHiddenFalse(@Param("username") String username);

    @Transactional
    @Modifying
    void deleteByEmail(String email);

    @Query("SELECT u FROM User u JOIN Comment c ON c.user.id= u.id WHERE c.id =:id")
    User findUserByCommentId(@Param("id") Long id);
}