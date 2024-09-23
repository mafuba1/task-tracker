package ru.nasrulaev.email_sender.model;

public class RegistrationEmailContext {

    private String to;

    public RegistrationEmailContext() {
    }

    public RegistrationEmailContext(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}