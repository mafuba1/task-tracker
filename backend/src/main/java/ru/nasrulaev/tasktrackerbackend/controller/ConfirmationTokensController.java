package ru.nasrulaev.tasktrackerbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.ConfirmTokenRequest;
import ru.nasrulaev.tasktrackerbackend.dto.ResendTokenRequest;
import ru.nasrulaev.tasktrackerbackend.exception.UserAlreadyConfirmed;
import ru.nasrulaev.tasktrackerbackend.service.ConfirmationTokensService;

@RestController
@RequestMapping("/api/tokens")
public class ConfirmationTokensController {
    private final ConfirmationTokensService confirmationTokensService;

    @Autowired
    public ConfirmationTokensController(ConfirmationTokensService confirmationTokensService) {
        this.confirmationTokensService = confirmationTokensService;
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse confirmToken(@RequestBody ConfirmTokenRequest request) throws UserAlreadyConfirmed {
        return new AuthenticationResponse(
                confirmationTokensService.confirm(
                        request.getToken()
                )
        );
    }

    @PostMapping(value = "/resend", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resendToken(@RequestBody ResendTokenRequest request) {
        confirmationTokensService.resend(
                request.getEmail()
        );
    }
}
