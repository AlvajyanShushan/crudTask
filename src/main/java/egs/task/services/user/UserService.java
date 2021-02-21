package egs.task.services.user;

import egs.task.models.entities.User;
import egs.task.services.CommonService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService extends CommonService<User> {

    UserDetails loadUserByUsername(String username);

    Optional<User> getByUsername(String phoneOrEmail);

    void deleteByUsername(String deviceId) throws Exception;

    void deleteUser(User user);
}