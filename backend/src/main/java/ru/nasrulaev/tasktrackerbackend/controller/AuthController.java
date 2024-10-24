package ru.nasrulaev.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationRequest;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.RegistrationResponse;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.service.AuthenticationService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
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

    private User convertDTOtoUser(AuthenticationRequest authenticationRequest) {
        return modelMapper.map(
                authenticationRequest,
                User.class
        );
    }
}
