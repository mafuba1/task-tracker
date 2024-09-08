package ru.nasrulaev.tasktrackerbackend.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String msg) {
        super(msg);
    }

    public TaskNotFoundException() {
        super();
    }
}
