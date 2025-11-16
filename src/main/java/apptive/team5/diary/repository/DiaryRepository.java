package apptive.team5.diary.repository;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    Page<DiaryEntity> findByUser(UserEntity user, Pageable pageable);

    List<DiaryEntity> findByUserId(Long userId);

    Page<DiaryEntity> findByUserIdAndScopeIn(Long userId, List<DiaryScope> scopes, Pageable pageable);

    List<DiaryEntity> findByUserIdAndCreateDateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("select count(d) from DiaryEntity d where d.user.id = :userId")
    int countByUserId(Long userId);

    @Query("delete from DiaryEntity d where d.user.id = :userId")
    @Modifying(clearAutomatically = true)
    void deleteByUserId(Long userId);

    @Query("select d from DiaryEntity d where d.user.id in :userIds")
    Page<DiaryEntity> findByUserIdsPage(Set<Long> userIds, Pageable pageable);

    @Query("select d from DiaryEntity d join fetch d.user where d.user.id in :userIds")
    Page<DiaryEntity> findByUserIdsWithUserPage(Set<Long> userIds, Pageable pageable);
}
