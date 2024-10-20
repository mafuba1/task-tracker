package ru.nasrulaev.tasktrackerbackend.dto;

public class GetUserInfoResponse{
    private long id;
    private String email;
    private boolean subscribed;

    public GetUserInfoResponse() {
    }

    public GetUserInfoResponse(long id, String email, boolean subscribed) {
        this.id = id;
        this.email = email;
        this.subscribed = subscribed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
