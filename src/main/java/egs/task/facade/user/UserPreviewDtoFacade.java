package egs.task.facade.user;

import egs.task.convert.UserConvert;
import egs.task.models.dtos.user.UserListPreviewDto;
import egs.task.models.dtos.user.UserSearchDto;
import egs.task.models.dtos.user.UserSpecification;
import egs.task.models.entities.User;
import egs.task.repositories.BookRepository;
import egs.task.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserPreviewDtoFacade {
    @Lazy
    private final UserRepository userRepository;
    @Lazy
    private final BookRepository bookRepository;

    public UserPreviewDtoFacade(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Page<UserListPreviewDto> getAllUsers(UserSearchDto userSearchDto, Pageable pageable) {
        Page<User> users = userRepository.findAll(UserSpecification.searchUser(userSearchDto), pageable);
        return UserConvert.convertToPagePreviewDto(users, bookRepository);
    }
}
