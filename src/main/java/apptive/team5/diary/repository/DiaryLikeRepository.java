package apptive.team5.diary.repository;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DiaryLikeRepository extends JpaRepository<DiaryLikeEntity, Long> {

    Optional<DiaryLikeEntity> findByUserAndDiary(UserEntity user, DiaryEntity diary);

    boolean existsByUserAndDiary(UserEntity user, DiaryEntity diary);

    @Query("""
            SELECT dl.diary.id
            FROM DiaryLikeEntity dl
            WHERE dl.user.id = :userId
            AND dl.diary.id IN :diaryIds
    """)
    Set<Long> findLikedDiaryIdsByUser(
            @Param("userId")
            Long userId,
            @Param("diaryIds")
            List<Long> diaryIds
    );

    @Modifying
    @Query("""
            DELETE FROM DiaryLikeEntity dl
            WHERE dl.user.id = :userId
    """)
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("""
            DELETE FROM DiaryLikeEntity dl
            WHERE dl.diary.id = : diaryId
    """)
    void deleteByDiaryId(@Param("diaryId") Long diaryId);
}
