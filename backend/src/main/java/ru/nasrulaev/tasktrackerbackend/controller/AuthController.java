package ru.nasrulaev.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationRequest;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.GetUserInfoResponse;
import ru.nasrulaev.tasktrackerbackend.dto.RegistrationResponse;
import ru.nasrulaev.tasktrackerbackend.exception.UnauthorizedException;
import ru.nasrulaev.tasktrackerbackend.exception.UserAlreadyConfirmed;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;
import ru.nasrulaev.tasktrackerbackend.service.AuthenticationService;
import ru.nasrulaev.tasktrackerbackend.service.ConfirmationTokensService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;
    private final ConfirmationTokensService confirmationTokensService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, ModelMapper modelMapper, ConfirmationTokensService confirmationTokensService) {
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
        this.confirmationTokensService = confirmationTokensService;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse register(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        authenticationService.signUp(
                convertDTOtoUser(authenticationRequest)
        );

        return new RegistrationResponse(
                "To proceed the registration please confirm your email: " + authenticationRequest.getEmail()
        );
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return new AuthenticationResponse(
                authenticationService.signIn(
                        convertDTOtoUser(authenticationRequest)
                )
        );
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse confirmToken(@RequestParam(name = "token") String token) throws UserAlreadyConfirmed {
        return new AuthenticationResponse(
                confirmationTokensService.confirm(token)
        );
    }

    @GetMapping(value = "/user", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GetUserInfoResponse getUserInfo(@AuthenticationPrincipal PersonDetails personDetails) throws UnauthorizedException {
        if (personDetails == null) throw new UnauthorizedException("Unauthorized");

        User user = personDetails.getUser();
        return modelMapper.map(user, GetUserInfoResponse.class);
    }

    private User convertDTOtoUser(AuthenticationRequest authenticationRequest) {
        return modelMapper.map(
                authenticationRequest,
                User.class
        );
    }
}
