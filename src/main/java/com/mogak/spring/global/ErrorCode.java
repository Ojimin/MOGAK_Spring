package com.mogak.spring.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 성공 코드
     * */
    SUCCESS(HttpStatus.OK, "success", "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "created", "요청에 성공했으며 리소스가 정상적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "accepted", "요청에 성공했으나 처리가 완료되지 않았습니다."),

    /**
     * 유저 에러
     * */
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 사용자입니다"),
    NOT_EXIST_JOB(HttpStatus.NOT_FOUND, "U002", "존재하지 않는 직업입니다"),
    NOT_EXIST_ADDRESS(HttpStatus.NOT_FOUND, "U003","존재하지 않는 지역입니다"),
    NOT_VALID_NICKNAME(HttpStatus.CONFLICT, "U004","올바른 닉네임이 아닙니다"),
    NOT_VALID_EMAIL(HttpStatus.CONFLICT, "U005","올바른 이메일 형식이 아닙니다"),
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "U006",  "이미 존재하는 유저입니다"),
    NOT_VALID_APPLE_CLAIMS(HttpStatus.CONFLICT, "U007","올바르지 않은 apple oauth claims 형식입니다"),

    /**
     * 모다라트 에러
     * */
    NOT_EXIST_MODARAT(HttpStatus.BAD_REQUEST, "A001", "존재하지 않는 모다라트입니다"),
    EXCEED_MAX_NUM_MODARAT(HttpStatus.BAD_REQUEST, "A002", "모다라트 최대 글자수 100자를 초과하였습니다"),
    
    /**
     * 모각 에러
     * */
    NOT_EXIST_CATEGORY(HttpStatus.NOT_FOUND, "M001","존재하지 않는 카테고리입니다"),
    NOT_EXIST_OTHER_CATEGORY(HttpStatus.BAD_REQUEST, "M002", "기타 카테고리가 존재하지 않습니다"),
    WRONG_STATE_CHANGE(HttpStatus.BAD_REQUEST, "M003", "잘못된 상태 변경입니다"),
    NOT_EXIST_MOGAK(HttpStatus.NOT_FOUND, "M004", "존재하지 않는 모각입니다"),
    CAN_MODIFIED_BEFORE_START_MOGAK(HttpStatus.BAD_REQUEST, "M005", "시작 날짜는 시작하기 전에만 수정 가능합니다"),
    NOT_VALID_START_DATE(HttpStatus.BAD_REQUEST, "M006", "잘못 입력된 시작 날짜입니다"),
    INVERSE_START_END(HttpStatus.BAD_REQUEST, "M007", "시작 날짜와 종료 날짜가 역전 되었습니다"),
    EXCEED_MAX_MOGAK(HttpStatus.CONFLICT, "J012", "생성 가능한 모각의 최대 갯수는 8개 입니다"),

    /**
     * 조각 에러
     * */
    NOT_START_JOGAK(HttpStatus.BAD_REQUEST, "J001", "시작하지 않은 조각입니다"),
    ALREADY_START_JOGAK(HttpStatus.CONFLICT, "J002", "이미 시작한 조각입니다"),
    ALREADY_END_JOGAK(HttpStatus.CONFLICT, "J003", "이미 종료한 조각입니다"),
    WRONG_START_MIDNIGHT_JOGAK(HttpStatus.BAD_REQUEST, "J004", "자정을 넘어서 조각을 시작할 수 없습니다"),
    NOT_EXIST_JOGAK(HttpStatus.NOT_FOUND, "J005", "존재하지 않는 조각입니다"),
    OVERDUE_DEADLINE_JOGAK(HttpStatus.BAD_REQUEST, "J006", "기한을 넘긴 조각입니다"),
    WRONG_CREATE_JOGAK(HttpStatus.BAD_REQUEST, "J007", "진행중인 모각만 조각을 생성할 수 있습니다"),
    NOT_VALID_UPDATE_JOGAK(HttpStatus.BAD_REQUEST, "J008", "유효하지 않은 조각 수정입니다"),
    NOT_VALID_PERIOD(HttpStatus.BAD_REQUEST, "J009", "유효하지 않은 반복주기입니다"),
    NOT_VALID_DAILY_JOGAK(HttpStatus.BAD_REQUEST, "J010", "유효하지 않은 루틴의 조각입니다"),
    NOT_SUCCESS_DAILY_JOGAK(HttpStatus.CONFLICT, "J011", "성공하지 않은 데일리 조각입니다"),
    EXCEED_MAX_JOGAK(HttpStatus.CONFLICT, "J012", "생성 가능한 조각의 최대 갯수는 8개 입니다"),
    NOT_EXIST_ROUTINES(HttpStatus.CONFLICT, "J013", "루틴이 설정된 경우 요일이 필요합니다"),

    /**
     * 게시물 에러
     * */
    EXCEED_MAX_NUM_POST(HttpStatus.BAD_REQUEST, "P001", "최대 글자수 350자를 초과하였습니다"),
    NOT_HAVE_IMAGE(HttpStatus.BAD_REQUEST, "P002", "이미지가 존재하지 않습니다"),
    NOT_EXIST_POST(HttpStatus.NOT_FOUND, "P003", "존재하지 않는 게시물입니다"),
    ALREADY_CREATE_LIKE(HttpStatus.INTERNAL_SERVER_ERROR, "P004", "이미 좋아요를 누른 게시물입니다"),

    /**
     * 댓글 에러
     * */
    EXCEED_MAX_NUM_COMMENT(HttpStatus.BAD_REQUEST, "C001", "최대 글자수 200자를 초과하였습니다"),
    NOT_EXIST_COMMENT(HttpStatus.NOT_FOUND, "C002", "존재하지 않는 댓글입니다"),

    /**
     * 토큰 에러
     * */
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "T001", "잘못된 형식의 토큰입니다"),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "T002", "만료된 토큰입니다"),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "T003", "토큰이 존재하지 않습니다"),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "T004", "권한이 부여되지 않았습니다"),
    LOGOUT_TOKEN(HttpStatus.FORBIDDEN, "T005", "로그아웃된 토큰입니다"),
    APPLE_JWT_WRONG_TOKEN(HttpStatus.BAD_REQUEST, "T006", "Apple JWT 값의 alg, kid 정보가 올바르지 않습니다."),
    WRONG_APPLE_ENCODE(HttpStatus.CONFLICT, "T007", "Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."),
    WRONG_APPLE_PUBLIC_KEY(HttpStatus.CONFLICT, "T008", "Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다."),
    INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST,"T009", "Apple OAuth Identity Token 형식이 올바르지 않습니다."),
    EXPIRE_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST,"T010", "Apple 로그인 중 Identity Token 유효기간이 만료됐습니다."),




    /**
     * 팔로우 에러
     * */
    ALREADY_CREATE_FOLLOW(HttpStatus.CONFLICT, "F001", "이미 존재하는 팔로우입니다"),
    NOT_EXIST_FOLLOW(HttpStatus.NOT_FOUND, "F002", "존재하지 않는 팔로우입니다"),


    NOT_EXIST_DAY(HttpStatus.NOT_FOUND, "Z001", "존재하지 않는 요일입니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Z002", "잘못된 요청입니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Z003","찾을 수 없습니다"),
    NOT_SUPPORTED_METHOD_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "Z004", "지원하지 않는 HTTP Method 요청입니다."),
    INVALID_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "Z005", "입력값이 유효하지 않습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Z500", "서버와의 연결에 실패했습니다")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    public static ErrorCode findByMessage(String message) {
        for (ErrorCode response : values()) {
            if (message.equals(response.message)) {
                return response;
            }
        }
        return null;
    }
}
