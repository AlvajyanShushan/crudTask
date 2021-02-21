package egs.task.repositories;

import egs.task.models.entities.Guest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends CommonRepository<Guest>{
    Optional<Guest> findByEmail(String deviceId);
}
