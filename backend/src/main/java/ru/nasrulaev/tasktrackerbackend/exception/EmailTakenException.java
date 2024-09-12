package ru.nasrulaev.tasktrackerbackend.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailTakenException extends AuthenticationException {
    public EmailTakenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailTakenException(String msg) {
        super(msg);
    }
}
