package com.employee.exceptions;

import java.time.LocalDateTime;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private int errorCode;
    private String message;
    private String description;

    public ErrorDetails(LocalDateTime timestamp, int errorCode, String message, String description) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
