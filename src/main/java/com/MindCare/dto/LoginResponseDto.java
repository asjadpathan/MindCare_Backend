package com.MindCare.dto;

public class LoginResponseDto {
    private boolean success;
    private String message;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResponseDto(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }
}
