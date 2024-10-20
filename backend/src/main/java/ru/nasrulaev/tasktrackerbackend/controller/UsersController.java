package ru.nasrulaev.tasktrackerbackend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @PatchMapping("/subscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribe(@AuthenticationPrincipal PersonDetails personDetails) {
        usersService.subscribe(personDetails.getUser());
    }

    @PatchMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@AuthenticationPrincipal PersonDetails personDetails) {
        usersService.unsubscribe(personDetails.getUser());
    }


    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GetUserInfoResponse getUserInfo(@AuthenticationPrincipal PersonDetails personDetails) throws UnauthorizedException {
        if (personDetails == null) throw new UnauthorizedException("Unauthorized");

        User user = personDetails.getUser();
        return modelMapper.map(user, GetUserInfoResponse.class);
    }
}