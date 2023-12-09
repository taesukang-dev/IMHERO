package com.imhero.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER(HttpStatus.CONFLICT, "User is duplicated"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "email not found"),
    SHOW_NOT_FOUND(HttpStatus.NOT_FOUND, "show not found"),
    SHOW_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "show detail not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    INSUFFICIENT_SEAT(HttpStatus.BAD_REQUEST, "Insufficient seat quantity"),
    EXCEEDED_SEAT_CANCELLATION(HttpStatus.BAD_REQUEST, "Exceeded seat cancellation"),
    GRADE_NOT_FOUND(HttpStatus.NOT_FOUND, "Grade not found"),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "Seat not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    ALREADY_DELETED(HttpStatus.CONFLICT, "Already deleted"),
    UNAUTHORIZED_BEHAVIOR(HttpStatus.UNAUTHORIZED, "Unauthorized behavior"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private HttpStatus status;
    private String message;
}
