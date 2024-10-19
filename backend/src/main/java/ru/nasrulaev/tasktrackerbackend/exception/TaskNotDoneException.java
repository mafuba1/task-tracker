package ru.nasrulaev.tasktrackerbackend.exception;

public class TaskNotDoneException extends RuntimeException {
    public TaskNotDoneException(String msg) {
        super(msg);
    }

    public TaskNotDoneException() {}
}
