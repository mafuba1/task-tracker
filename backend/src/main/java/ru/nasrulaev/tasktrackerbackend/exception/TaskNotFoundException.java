package ru.nasrulaev.tasktrackerbackend.exception;

import jakarta.persistence.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(String msg) {
        super(msg);
    }

    public TaskNotFoundException() {
        super();
    }
}
