package ru.nasrulaev.tasktrackerbackend.dto;

public class ResendTokenRequest {
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
