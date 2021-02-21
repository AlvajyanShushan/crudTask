package egs.task.services.userDevice;

import egs.task.enums.OsTypeEnum;
import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.dtos.device.DeviceCreatingDto;
import egs.task.models.entities.User;
import egs.task.models.entities.UserDevice;
import egs.task.repositories.UserDeviceRepository;
import egs.task.repositories.UserRepository;
import egs.task.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserDeviceServiceImpl extends AbstractService<UserDevice, UserDeviceRepository> implements UserDeviceService {
    @Lazy
    private UserDeviceRepository repository;
    @Lazy
    private UserRepository userRepository;

    @Autowired
    public UserDeviceServiceImpl(UserDeviceRepository repository, UserRepository userRepository) {
        super(repository);
        this.repository = repository;
        this.userRepository = userRepository;
    }
    @Override
    public Optional<UserDevice> findByDeviceId(String deviceId) {
        return repository.findByDeviceId(deviceId);
    }

    @Override
    public void updateUserDevice(UserDevice userDevice, Long id) throws EntityNotFoundException {
        UserDevice userDeviceGetById = getById(id);
        userDeviceGetById.setDeviceToken(userDevice.getDeviceToken());
        userDeviceGetById.setDeviceId(userDevice.getDeviceId());
        userDeviceGetById.setLanguageName(userDevice.getLanguageName());
        userDeviceGetById.setOsTypeEnum(userDevice.getOsTypeEnum());
        userDeviceGetById.setModelName(userDevice.getModelName());
        userDeviceGetById.setUser(userDevice.getUser());
    }

    @Override
    public void saveDevice(DeviceCreatingDto deviceCreatingDto, Authentication authentication) throws EntityNotFoundException {
        Optional<UserDevice> optionalUserDevice = findByDeviceId(deviceCreatingDto.getDeviceId());
        UserDevice device = UserDevice.builder()
                .deviceId(deviceCreatingDto.getDeviceId())
                .deviceToken(deviceCreatingDto.getDeviceToken())
                .modelName(deviceCreatingDto.getModelName())
                .languageName(deviceCreatingDto.getLanguageName())
                .osTypeEnum(OsTypeEnum.valueOf(deviceCreatingDto.getOsTypeId()).orElseThrow()).build();
        device.setHidden(false);
        device.setUser(userRepository.findByEmailAndHiddenFalse(authentication.getName()).orElseThrow(()
                -> new EntityNotFoundException(User.class, "username", String.valueOf(authentication.getName()))));
        if (optionalUserDevice.isEmpty()) {
            save(device);
        } else {
            updateUserDevice(device, optionalUserDevice.get().getId());
        }
    }

    @Override
    public Boolean deleteByDeviceId(String deviceId) throws EntityNotFoundException {
        UserDevice device = findByDeviceId(deviceId).orElseThrow(() -> new EntityNotFoundException(UserDevice.class, " deviceId ", String.valueOf(deviceId)));
        repository.delete(device);
        return true;
    }
}