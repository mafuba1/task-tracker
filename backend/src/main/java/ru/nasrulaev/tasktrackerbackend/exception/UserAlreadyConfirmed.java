package ru.nasrulaev.tasktrackerbackend.exception;

import javax.naming.AuthenticationException;

public class UserAlreadyConfirmed extends AuthenticationException {
    public UserAlreadyConfirmed(String msg) {
        super(msg);
    }

    public UserAlreadyConfirmed() {

    }
}
