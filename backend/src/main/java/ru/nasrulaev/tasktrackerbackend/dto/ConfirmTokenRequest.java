package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotEmpty;

public class ConfirmTokenRequest {

    @NotEmpty
    private String token;

    public ConfirmTokenRequest() {
    }

    public ConfirmTokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
