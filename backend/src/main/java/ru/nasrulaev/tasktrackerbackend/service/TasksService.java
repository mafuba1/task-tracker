package ru.nasrulaev.tasktrackerbackend.service;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.model.Task;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.repository.TasksRepository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TasksService {

    private final TasksRepository tasksRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public Task findOne(long id) {
        return tasksRepository
                .findById(id)
                .orElse(null);
    }

    public List<Task> findTasksByOwner(User owner) {
        return tasksRepository
                .findTasksByOwner(owner)
                .orElse(
                        Collections.emptyList()
                );
    }

    @Transactional
    public void save(Task task) {
        tasksRepository.save(task);
    }

    @Transactional
    public void markDone(@Nonnull Task task) {
        task.setDone_timestamp(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );
        tasksRepository.save(task);
    }

    @Transactional
    public void deleteById(long id) {
        tasksRepository.deleteById(id);
    }

}
