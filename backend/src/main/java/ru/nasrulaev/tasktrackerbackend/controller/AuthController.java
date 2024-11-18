package ru.nasrulaev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationRequest;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;
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

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Email taken",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
    })
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

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Bad credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "User disabled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
    })
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
