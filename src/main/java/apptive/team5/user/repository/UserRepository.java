package apptive.team5.user.repository;

import apptive.team5.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdentifier(String identifier);
    Optional<UserEntity> findByTag(String tag);
}
