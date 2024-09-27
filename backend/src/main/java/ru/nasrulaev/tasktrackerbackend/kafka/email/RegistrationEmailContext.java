package ru.nasrulaev.tasktrackerbackend.kafka.email;

public class RegistrationEmailContext {

    private String to;
    private String token;

    public RegistrationEmailContext() {
    }

    public RegistrationEmailContext(String to, String token) {
        this.to = to;
        this.token = token;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}