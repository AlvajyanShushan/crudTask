package egs.task.services.userDevice;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.device.DeviceCreatingDto;
import egs.task.models.entities.UserDevice;
import egs.task.services.CommonService;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserDeviceService extends CommonService<UserDevice> {
    Optional<UserDevice> findByDeviceId(String deviceId);

    void updateUserDevice(UserDevice device, Long id) throws EntityNotFoundException;

    void saveDevice(DeviceCreatingDto deviceCreatingDto, Authentication authentication) throws EntityNotFoundException;

    Boolean deleteByDeviceId(String deviceId) throws EntityNotFoundException;
}