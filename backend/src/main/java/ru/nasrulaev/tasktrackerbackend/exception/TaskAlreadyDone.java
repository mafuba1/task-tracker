package ru.nasrulaev.tasktrackerbackend.exception;

public class TaskAlreadyDone extends RuntimeException {
    public TaskAlreadyDone(String msg) {
        super(msg);
    }

    public TaskAlreadyDone() {}
}
