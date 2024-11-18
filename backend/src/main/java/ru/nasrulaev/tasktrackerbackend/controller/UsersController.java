package ru.nasrulaev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.ErrorDTO;
import ru.nasrulaev.tasktrackerbackend.dto.GetUserInfoResponse;
import ru.nasrulaev.tasktrackerbackend.exception.UnauthorizedException;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;
import ru.nasrulaev.tasktrackerbackend.service.UsersService;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    private final ModelMapper modelMapper;
    private final UsersService usersService;

    @Autowired
    public UsersController(ModelMapper modelMapper, UsersService usersService) {
        this.modelMapper = modelMapper;
        this.usersService = usersService;
    }

    @Operation(summary = "Subscribe user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User subscribed",
                    content = @Content()
            ),
            @ApiResponse(responseCode = "409", description = "User already subscribed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @PatchMapping("/subscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribe(@AuthenticationPrincipal PersonDetails personDetails) {
        usersService.subscribe(personDetails.getUser());
    }

    @Operation(summary = "Unsubscribe user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User unsubscribed",
                    content = @Content()
            ),
            @ApiResponse(responseCode = "409", description = "User not subscribed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @PatchMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@AuthenticationPrincipal PersonDetails personDetails) {
        usersService.unsubscribe(personDetails.getUser());
    }


    @Operation(summary = "Get authenticated user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received user info",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetUserInfoResponse.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GetUserInfoResponse getUserInfo(@AuthenticationPrincipal PersonDetails personDetails) throws UnauthorizedException {
        if (personDetails == null) throw new UnauthorizedException("Unauthorized");

        User user = personDetails.getUser();
        return modelMapper.map(user, GetUserInfoResponse.class);
    }
}