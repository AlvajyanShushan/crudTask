package egs.task.utils;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.entities.User;
import egs.task.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindUser {
    @Lazy
    private final UserRepository userRepository;


    public FindUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByToken(Authentication authentication) throws EntityNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmailAndHiddenFalse(authentication.getName());
        Optional<User> optionalUserByPhone = userRepository.findByPhoneNumberAndHiddenFalse(authentication.getName());
        if (optionalUser.isEmpty() && optionalUserByPhone.isEmpty()) {
            throw new EntityNotFoundException(User.class, "userName", authentication.getName());
        }
        return optionalUser.orElseGet(() -> optionalUserByPhone.orElseGet(null));
    }
}