package ru.nasrulaev.tasktrackerbackend.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.exception.ConfirmationTokenNotFoundException;
import ru.nasrulaev.tasktrackerbackend.exception.ConfirmationTokenExpiredException;
import ru.nasrulaev.tasktrackerbackend.exception.UserAlreadyConfirmed;
import ru.nasrulaev.tasktrackerbackend.kafka.KafkaService;
import ru.nasrulaev.tasktrackerbackend.kafka.email.RegistrationEmailContext;
import ru.nasrulaev.tasktrackerbackend.model.ConfirmationToken;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.repository.ConfirmationTokensRepository;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class ConfirmationTokensService {

    static final Log log = LogFactory.getLog(ConfirmationTokensService.class);

    private final ConfirmationTokensRepository confirmationTokenRepository;
    private final UsersService usersService;
    private final JwtService jwtService;
    private final KafkaService kafkaService;


    @Autowired
    public ConfirmationTokensService(ConfirmationTokensRepository confirmationTokenRepository, UsersService usersService, JwtService jwtService, KafkaService kafkaService) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.usersService = usersService;
        this.jwtService = jwtService;
        this.kafkaService = kafkaService;
    }

    private String generateToken() {
        BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
        Charset US_ASCII = StandardCharsets.US_ASCII;
        return new String(
                Base64.getEncoder().encode(
                                DEFAULT_TOKEN_GENERATOR.generateKey()
                ),
                US_ASCII
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void sendToken(User user) {
        String token = generateToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24)
        );

        kafkaService.sendMessage(
                new RegistrationEmailContext(
                        user.getEmail(),
                        token
                )
        );

        confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public String confirm(String token) throws UserAlreadyConfirmed {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException("Token not found"));

        verifyToken(confirmationToken);
        User user = confirmationToken.getUser();
        usersService.enable(user);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        return jwtService.generateToken(
                new PersonDetails(user)
        );
    }

    @Transactional
    public void resend(String email) {
        log.info("Resend request by " + email);
        ConfirmationToken token = confirmationTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException("Token not found"));

        if (token.getUser().isEnabled()) throw new IllegalStateException("User already confirmed");

        token.setToken(
                generateToken());
        token.setCreatedAt(
                LocalDateTime.now());
        token.setExpiresAt(
                LocalDateTime.now().plusHours(24));

        confirmationTokenRepository.save(token);

        kafkaService.sendMessage(
                new RegistrationEmailContext(
                        email,
                        token.getToken()
                )
        );
    }

    private void verifyToken(ConfirmationToken confirmationToken) {
        if (confirmationToken.getExpiresAt()
                .isAfter(LocalDateTime.now())
        ) return;

        throw new ConfirmationTokenExpiredException(
                "Registration confirmation token expired at: " + confirmationToken.getExpiresAt()
        );
    }
}
