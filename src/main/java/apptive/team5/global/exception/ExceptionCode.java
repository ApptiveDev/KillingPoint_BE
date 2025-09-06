package apptive.team5.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    NOT_FOUND_USER("존재하지 않는 회원입니다."),
    NOT_EXIST_REFRESH_TOKEN("리프래시 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN("잘못된 리프래시 토큰입니다.");

    private final String description;


    ExceptionCode(String description) {
        this.description = description;
    }
}
