package ru.nasrulaev.tasktrackerbackend.exception;

public class UserAlreadySubscribed extends RuntimeException {
    public UserAlreadySubscribed(String userAlreadySubscribed) {
        super(userAlreadySubscribed);
    }

    public UserAlreadySubscribed() {}
}
