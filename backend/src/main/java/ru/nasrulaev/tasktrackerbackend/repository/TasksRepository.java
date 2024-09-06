package ru.nasrulaev.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nasrulaev.tasktrackerbackend.model.Task;
import ru.nasrulaev.tasktrackerbackend.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findTasksByOwner(User owner);
}
