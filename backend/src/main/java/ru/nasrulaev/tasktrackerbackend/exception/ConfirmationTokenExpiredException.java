package ru.nasrulaev.tasktrackerbackend.exception;

import org.springframework.security.core.AuthenticationException;

public class ConfirmationTokenExpiredException extends AuthenticationException {
    public ConfirmationTokenExpiredException(String msg) {
        super(msg);
    }
}
