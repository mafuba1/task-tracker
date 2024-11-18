package ru.nasrulaev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.ConfirmTokenRequest;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;
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

    @Operation(summary = "Approve token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token approved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Token expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "User already enabled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Empty token", content = @Content())
    })
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse confirmToken(
            @Parameter(description = "Token to approve") @RequestBody @Valid ConfirmTokenRequest request
    ) throws UserAlreadyConfirmed {
        return new AuthenticationResponse(
                confirmationTokensService.confirm(
                        request.getToken()
                )
        );
    }

    @Operation(summary = "Resend token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token resent",
                    content = @Content()
            ),
            @ApiResponse(responseCode = "404", description = "Token not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "User already enabled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Empty email",
                    content = @Content()
            )
    })
    @PostMapping(value = "/resend", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resendToken(
            @Parameter(description = "User email") @RequestBody @Valid ResendTokenRequest request
    ) throws UserAlreadyConfirmed {
        confirmationTokensService.resend(
                request.getEmail()
        );
    }
}
