package ru.nasrulaev.tasktrackerbackend.exception;

public class UserNotSubscribed extends RuntimeException {
    public UserNotSubscribed(String userNotSubscribed) {
        super(userNotSubscribed);
    }
    public UserNotSubscribed() {}
}
