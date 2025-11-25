package apptive.team5.diary.mapper;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DiaryResponseMapper {

    @FunctionalInterface
    private interface DiaryResponseDtoMapper {
        DiaryResponseDto map(DiaryEntity diary, boolean isLiked, Long likeCount, Long currentUserId);
    }

    private Page<DiaryResponseDto> mapToResponseDto(
            Page<DiaryEntity> diaryPage,
            Set<Long> likedDiaryIds,
            Map<Long, Long> likeCountsMap,
            Long currentUserId,
            DiaryResponseDtoMapper mapper
    ) {
        if (diaryPage.isEmpty()) {
            return Page.empty(diaryPage.getPageable());
        }

        return diaryPage.map(diary ->
                mapper.map(
                        diary,
                        likedDiaryIds.contains(diary.getId()),
                        likeCountsMap.getOrDefault(diary.getId(), 0L),
                        currentUserId
                )
        );
    }
}
