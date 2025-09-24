package apptive.team5.diary.repository;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    Page<DiaryEntity> findByUser(UserEntity user, Pageable pageable);
}
