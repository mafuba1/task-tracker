package ru.nasrulaev.tasktrackerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.exception.EmailTakenException;
import ru.nasrulaev.tasktrackerbackend.exception.UserAlreadyConfirmed;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.repository.UsersRepository;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User save(User user) {
        if (usersRepository.existsByEmail(user.getEmail())) throw new EmailTakenException("Email is taken");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void enable(User user) throws UserAlreadyConfirmed {
        if (user.isEnabled()) throw new UserAlreadyConfirmed("User already confirmed");

        user.setEnabled(true);
        usersRepository.save(user);
    }
}
