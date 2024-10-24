package ru.nasrulaev.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nasrulaev.tasktrackerbackend.model.ConfirmationToken;

import java.util.Optional;

@Repository
public interface ConfirmationTokensRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUserEmail(String email);
}
