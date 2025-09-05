package apptive.team5.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_USER("존재하지 않는 회원입니다.");

    private final String description;


    ErrorCode(String description) {
        this.description = description;
    }
}
