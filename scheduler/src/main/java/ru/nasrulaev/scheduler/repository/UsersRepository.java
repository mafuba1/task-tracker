package ru.nasrulaev.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nasrulaev.scheduler.model.User;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.tasks WHERE u.subscribed = true")
    List<User> findSubscribedUsers();
}
