package ru.nasrulaev.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationRequest;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.dto.GetUserInfoResponse;
import ru.nasrulaev.tasktrackerbackend.dto.RegistrationForm;
import ru.nasrulaev.tasktrackerbackend.exception.UnauthorizedException;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;
import ru.nasrulaev.tasktrackerbackend.service.JwtService;
import ru.nasrulaev.tasktrackerbackend.service.PersonDetailsService;
import ru.nasrulaev.tasktrackerbackend.service.UsersService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final PersonDetailsService personDetailsService;
    private final UsersService usersService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(PersonDetailsService personDetailsService, UsersService usersService, JwtService jwtService, AuthenticationManager authenticationManager, ModelMapper modelMapper) {
        this.personDetailsService = personDetailsService;
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse register(@Valid @RequestBody RegistrationForm registrationRequest) {
        User user = modelMapper.map(registrationRequest, User.class);
        String jwtToken = jwtService.generateToken(
                new PersonDetails(user)
        );
        usersService.save(user);
        return new AuthenticationResponse(jwtToken);
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserDetails userDetails = personDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }

    @GetMapping(value = "/user", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public GetUserInfoResponse getUserInfo(@AuthenticationPrincipal PersonDetails personDetails) throws UnauthorizedException {
        if (personDetails == null) throw new UnauthorizedException("Unauthorized");

        User user = personDetails.getUser();
        return modelMapper.map(user, GetUserInfoResponse.class);
    }
}
