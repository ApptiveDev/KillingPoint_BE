package apptive.team5.diary.domain.model;

import apptive.team5.diary.domain.DiaryScope;

public record DiaryInfo(
        MusicBasicInfo musicBasicInfo,
        DiaryBasicInfo diaryBasicInfo,
        MusicPlayInfo musicPlayInfo
) {
}
