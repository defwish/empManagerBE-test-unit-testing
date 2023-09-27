package com.employee;

public enum ErrorCode {
    NOT_FOUND(404, "Resource not found"),
    //BAD_REQUEST(400, "Bad request"),
    //UNAUTHORIZED(401, "Unauthorized"),
    //FORBIDDEN(403, "Forbidden"),
    //INTERNAL_SERVER_ERROR(500, "Internal server error"),

    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
