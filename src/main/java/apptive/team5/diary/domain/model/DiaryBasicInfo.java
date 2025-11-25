package apptive.team5.diary.domain.model;

import apptive.team5.diary.domain.DiaryScope;

public record DiaryBasicInfo(
        String content,
        DiaryScope scope
) {
}
