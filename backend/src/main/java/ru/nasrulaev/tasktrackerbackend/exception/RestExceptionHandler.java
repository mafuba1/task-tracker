package ru.nasrulaev.tasktrackerbackend.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;

import java.sql.Timestamp;

@RestControllerAdvice
public class RestExceptionHandler {

    private final Log logger = LogFactory.getLog(this.getClass());

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

    @ExceptionHandler(TaskNotAccessibleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO taskNotAccessible(TaskNotAccessibleException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(TaskAlreadyDone.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO taskAlreadyDone(TaskAlreadyDone e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(TaskNotDoneException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO taskNotDoneException(TaskNotDoneException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO emailTaken(EmailTakenException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO emailNotFound(UsernameNotFoundException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO confirmationTokenNotFound(ConfirmationTokenNotFoundException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(ConfirmationTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO confirmationTokenExpired(ConfirmationTokenExpiredException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(UserAlreadyConfirmed.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO userAlreadyConfirmed(UserAlreadyConfirmed e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO badCredentialsException() {
        return new ErrorDTO(
                "Bad credentials",
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO disabledException(DisabledException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO illegalStateException(IllegalStateException e) {
        return new ErrorDTO(
                e.getMessage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGeneralException(Exception e) {
        logger.error("Error: ", e);
        return new ErrorDTO(
                "An unexpected error occurred.",
                new Timestamp(System.currentTimeMillis())
        );
    }
}
