package apptive.team5.diary.service;

import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
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

    public Page<DiaryResponse> getMyDiaries(String identifier, Pageable pageable) {

        UserEntity findUser = userLowService.findByIdentifier(identifier);

        /**
         * 조회된 유저를 바탕으로 음악 일기 찾기
         */


        DiaryResponse data1 =
                new DiaryResponse("목데이터1", "https://www.youtube-nocookie.com/embed/ki08IcGubwQ");

        DiaryResponse data2 =
                new DiaryResponse("목데이터2", "https://www.youtube-nocookie.com/embed/ki08IcGubwQ");

        DiaryResponse data3 =
                new DiaryResponse("목데이터1", "https://www.youtube-nocookie.com/embed/ki08IcGubwQ");

        DiaryResponse data4 =
                new DiaryResponse("목데이터2", "https://www.youtube-nocookie.com/embed/ki08IcGubwQ");

        List<DiaryResponse> datas = List.of(data1, data2, data3, data4);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), datas.size());
        List<DiaryResponse> subList = datas.subList(start, end);

        return new PageImpl<>(subList, pageable, datas.size());

    }
}
