package egs.task.repositories;

import egs.task.models.entities.UserDevice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceRepository extends CommonRepository<UserDevice>{

    Optional<UserDevice> findByDeviceId(String deviceId);
}
