package ru.nasrulaev.tasktrackerbackend.exception;

public class TaskNotAccessibleException extends RuntimeException{
    public TaskNotAccessibleException(String msg) {
        super(msg);
    }

    public TaskNotAccessibleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
