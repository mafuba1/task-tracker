package ru.nasrulaev.tasktrackerbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorDTO unauthorized(UnauthorizedException e) {
        return new ErrorDTO(e.getMessage());
    }
}
