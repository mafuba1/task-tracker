package ru.nasrulaev.tasktrackerbackend.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.kafka.KafkaService;
import ru.nasrulaev.tasktrackerbackend.kafka.email.RegistrationEmailContext;
import ru.nasrulaev.tasktrackerbackend.model.User;

@Service
public class AuthenticationService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final UsersService usersService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaService kafkaService;
    private final ConfirmationTokensService confirmationTokensService;

    @Autowired
    public AuthenticationService(UsersService usersService, JwtService jwtService, AuthenticationManager authenticationManager, KafkaService kafkaService, ConfirmationTokensService confirmationTokensService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.kafkaService = kafkaService;
        this.confirmationTokensService = confirmationTokensService;
    }

    @Transactional
    public void signUp(User user) {
        usersService.save(user);

        String token = confirmationTokensService
                .generateAndSaveToken(user)
                .getToken();

        kafkaService.sendMessage(
                new RegistrationEmailContext(
                        user.getEmail(),
                        token
                )
        );
    }

    public String signIn(User user) {
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()
            ));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return createToken(userDetails);
        } catch (Exception authException) {
            logger.error("Failure in login", authException);
            throw authException;
        }

    }

    private String createToken(UserDetails userDetails) {
        String jwt = jwtService.generateToken(userDetails);
        logger.debug("JWT generated successfully for user: " + userDetails.getUsername());

        return jwt;
    }
}