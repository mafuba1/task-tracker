package ru.nasrulaev.tasktrackerbackend.dto;

public class RegistrationResponse {
    private String msg;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
