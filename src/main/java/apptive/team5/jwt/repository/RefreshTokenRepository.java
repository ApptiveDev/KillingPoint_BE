package apptive.team5.jwt.repository;

import apptive.team5.jwt.domain.RefreshToken;
import apptive.team5.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.user = :user")
    void deleteByUser(UserEntity user);

    @Query("select r from RefreshToken r where r.user.identifier = :identifier")
    Optional<RefreshToken> findByUserIdentifier(String identifier);

    @Modifying(clearAutomatically = true)
    @Query("delete from RefreshToken r where r.createdDate < :date")
    void deleteExpiredRefreshToken(LocalDateTime date);

    @Query("select r from RefreshToken r where r.user.id = :userId")
    Optional<RefreshToken> findByUserId(Long userId);

}
