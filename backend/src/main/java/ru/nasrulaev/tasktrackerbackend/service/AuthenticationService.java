package ru.nasrulaev.tasktrackerbackend.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.model.User;

@Service
public class AuthenticationService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final UsersService usersService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokensService confirmationTokensService;

    @Autowired
    public AuthenticationService(UsersService usersService, JwtService jwtService, AuthenticationManager authenticationManager, ConfirmationTokensService confirmationTokensService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.confirmationTokensService = confirmationTokensService;
    }

    @Transactional
    public void signUp(User user) {
        user = usersService.save(user);
        confirmationTokensService.sendToken(user);
    }

    public String signIn(User user) {

        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()
            ));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return createToken(userDetails);
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("Bad credentials: ", badCredentialsException);
            throw badCredentialsException;
        } catch (DisabledException disabledException) {
            logger.error("User disabled: ", disabledException);
            throw disabledException;
        } catch (Exception e) {
            logger.error("Failure in login: ", e);
            throw e;
        }

    }

    private String createToken(UserDetails userDetails) {
        String jwt = jwtService.generateToken(userDetails);
        logger.debug("JWT generated successfully for user: " + userDetails.getUsername());

        return jwt;
    }
}