package ru.nasrulaev.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nasrulaev.tasktrackerbackend.model.Task;
import ru.nasrulaev.tasktrackerbackend.model.User;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByOwner(User owner);
}
