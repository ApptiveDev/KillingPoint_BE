package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import apptive.team5.youtube.dto.YoutubeSearchRequest;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import apptive.team5.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Transactional
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final UserLowService userLowService;
    private final DiaryRepository diaryRepository;
    private final YoutubeService youtubeService;

    public Page<DiaryResponse> getMyDiaries(String identifier, Pageable pageable) {

        UserEntity findUser = userLowService.findByIdentifier(identifier);

        /**
         * 조회된 유저를 바탕으로 음악 일기 찾기
         */


        DiaryResponse data1 =
                new DiaryResponse("artistName", "musicTitle", "https://www.sleek-mag.com/wp-content/uploads/2016/08/AlbumCovers_Blonde-1200x1200.jpg",
                        "목데이터1", "https://www.youtube-nocookie.com/embed/UXSdbtfw9Rc");

        DiaryResponse data2 =
                new DiaryResponse(
                        "artistName", "musicTitle", "https://www.sleek-mag.com/wp-content/uploads/2016/08/AlbumCovers_Blonde-1200x1200.jpg",
                        "목데이터2", "https://www.youtube-nocookie.com/embed/UXSdbtfw9Rc");

        DiaryResponse data3 =
                new DiaryResponse(
                        "artistName", "musicTitle", "https://www.sleek-mag.com/wp-content/uploads/2016/08/AlbumCovers_Blonde-1200x1200.jpg",
                        "목데이터1", "https://www.youtube-nocookie.com/embed/UXSdbtfw9Rc");

        DiaryResponse data4 =
                new DiaryResponse(
                        "artistName", "musicTitle", "https://www.sleek-mag.com/wp-content/uploads/2016/08/AlbumCovers_Blonde-1200x1200.jpg",
                        "목데이터2", "https://www.youtube-nocookie.com/embed/UXSdbtfw9Rc");

        List<DiaryResponse> datas = List.of(data1, data2, data3, data4);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), datas.size());
        List<DiaryResponse> subList = datas.subList(start, end);

        return new PageImpl<>(subList, pageable, datas.size());

    }

    public Page<DiaryResponse> getMyDiaries2(String identifier, Pageable pageable) {
        UserEntity foundUser = userLowService.findByIdentifier(identifier);

        return diaryRepository.findByUser(foundUser, pageable)
                .map(DiaryResponse::from);
    }

    public void createDiary(String identifier, DiaryRequest diaryRequest) {
        UserEntity foundUser = userLowService.findByIdentifier(identifier);

        YoutubeSearchRequest searchRequest = new YoutubeSearchRequest(diaryRequest.artist(), diaryRequest.musicTitle());
        List<YoutubeVideoResponse> youtubeVideoResponses = youtubeService.searchVideo(searchRequest);

        String videoUrl = youtubeVideoResponses.isEmpty() ? null : youtubeVideoResponses.getFirst().url();

        DiaryEntity diary = new DiaryEntity(
                diaryRequest.musicTitle(),
                diaryRequest.artist(),
                diaryRequest.albumImageUrl(),
                videoUrl,
                diaryRequest.content(),
                foundUser
        );

        diaryRepository.save(diary);
    }
}
