package egs.task.convert;

import egs.task.models.dtos.user.UserListPreviewDto;
import egs.task.models.entities.User;
import egs.task.repositories.BookRepository;
import org.springframework.data.domain.Page;

import static egs.task.utils.Configuration.userPath;

public class UserConvert {

    public static Page<UserListPreviewDto> convertToPagePreviewDto(Page<User> users, BookRepository bookRepository) {
        return users.map(user -> UserConvert.convertToPreviewDto(user, bookRepository));
    }

    private static UserListPreviewDto convertToPreviewDto(User user, BookRepository bookRepository) {
        return UserListPreviewDto.builder()
                .userId(user.getId())
                .firsName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl() != null ? userPath + user.getImageUrl() : null)
                .addedBooksCount(bookRepository.countAllUsersAddedBooks(user.getId()))
                .approvedBooksCount(bookRepository.countAllUsersAddedApprovedBooks(user.getId()))
                .build();
    }
}