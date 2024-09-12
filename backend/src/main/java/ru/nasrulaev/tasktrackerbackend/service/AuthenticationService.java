package ru.nasrulaev.tasktrackerbackend.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.nasrulaev.tasktrackerbackend.dto.AuthenticationResponse;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;

@Service
public class AuthenticationService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final UsersService usersService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthenticationService(UsersService usersService, JwtService jwtService, AuthenticationManager authenticationManager, PersonDetailsService personDetailsService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.personDetailsService = personDetailsService;
    }

    public AuthenticationResponse signUp(User user) {
        usersService.save(user);
        return authenticate(new PersonDetails(user));
    }

    public AuthenticationResponse signIn(User user) {
        UserDetails userDetails = personDetailsService.loadUserByUsername(user.getEmail());
        return authenticate(userDetails);
    }

    private AuthenticationResponse authenticate(UserDetails userDetails) {
        String jwt = jwtService.generateToken(userDetails);
        logger.debug("JWT generated: " + jwt);

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword()
            ));
            logger.debug(authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Failure in login", e);
        }

        return new AuthenticationResponse(jwt);
    }
}