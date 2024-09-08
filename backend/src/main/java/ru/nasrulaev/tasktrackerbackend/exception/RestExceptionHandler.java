package ru.nasrulaev.tasktrackerbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;

import java.sql.Timestamp;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorized(UnauthorizedException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO taskNotFound(TaskNotFoundException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
