package egs.task.facade.user;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.user.UserCreatingDto;
import egs.task.models.dtos.user.UserListPreviewDto;
import egs.task.models.dtos.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserFacadeBuilder {
    private final UserCreatingDtoFacade userCreatingDtoFacade;
    private final UserPreviewDtoFacade userPreviewDtoFacade;

    public UserFacadeBuilder(UserCreatingDtoFacade userCreatingDtoFacade,
                             UserPreviewDtoFacade userPreviewDtoFacade) {
        this.userCreatingDtoFacade = userCreatingDtoFacade;
        this.userPreviewDtoFacade = userPreviewDtoFacade;
    }

    public Long registration(UserCreatingDto userCreatingDto, String deviceId) throws Exception {
        return userCreatingDtoFacade.registration(userCreatingDto, deviceId);
    }

    public Boolean uploadImage(MultipartFile file, Authentication authentication) throws EntityNotFoundException {
        return userCreatingDtoFacade.uploadImage(file, authentication);
    }

    public Page<UserListPreviewDto> getAllUsers(Pageable pageable, UserSearchDto userSearchDto) {
        return userPreviewDtoFacade.getAllUsers(userSearchDto, pageable);
    }
}
