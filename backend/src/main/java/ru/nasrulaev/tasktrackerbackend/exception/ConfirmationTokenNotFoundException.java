package ru.nasrulaev.tasktrackerbackend.exception;

import jakarta.persistence.EntityNotFoundException;

public class ConfirmationTokenNotFoundException extends EntityNotFoundException {
    public ConfirmationTokenNotFoundException(String msg) {
        super(msg);
    }

    public ConfirmationTokenNotFoundException() {}
}
