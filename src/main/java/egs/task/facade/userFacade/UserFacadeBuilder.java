package egs.task.facade.userFacade;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.user.UserCreatingDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserFacadeBuilder {
    private final UserFacadeCreatingDto userFacadecreatingDto;

    public UserFacadeBuilder(UserFacadeCreatingDto userFacadecreatingDto) {
        this.userFacadecreatingDto = userFacadecreatingDto;
    }

    public Long registration(UserCreatingDto userCreatingDto, String deviceId) throws Exception {
        return userFacadecreatingDto.registration(userCreatingDto, deviceId);
    }

    public Boolean uploadImage(MultipartFile file, Authentication authentication) throws EntityNotFoundException {
        return userFacadecreatingDto.uploadImage(file, authentication);
    }
}
