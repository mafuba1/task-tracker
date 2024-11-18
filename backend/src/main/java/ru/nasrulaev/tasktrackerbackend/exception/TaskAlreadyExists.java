package ru.nasrulaev.tasktrackerbackend.exception;

public class TaskAlreadyExists extends RuntimeException {
    public TaskAlreadyExists(String msg) {
        super(msg);
    }

    public TaskAlreadyExists(String msg, Throwable cause) {}
}
