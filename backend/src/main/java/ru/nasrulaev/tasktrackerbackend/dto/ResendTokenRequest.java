package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotEmpty;

public class ResendTokenRequest {

    @NotEmpty
    private String email;

    public ResendTokenRequest() {
    }

    public ResendTokenRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
