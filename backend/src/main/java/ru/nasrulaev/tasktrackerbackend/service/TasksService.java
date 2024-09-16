package ru.nasrulaev.tasktrackerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.exception.TaskNotFoundException;
import ru.nasrulaev.tasktrackerbackend.exception.UnauthorizedException;
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

    private Task findOne(long id) {
        return tasksRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new TaskNotFoundException("Task with id '" + id + "' not found")
                );
    }

    public List<Task> findTasksByOwner(User owner) {
        return tasksRepository
                .findTasksByOwner(owner)
                .orElse(
                        Collections.emptyList()
                );
    }

    @Transactional
    public Task save(Task task) {
        return tasksRepository.save(task);
    }

    @Transactional
    public Task save(Task task, User user) {
        task.setOwner(user);
        return tasksRepository.save(task);
    }

    @Transactional
    public Task update(long id, Task updatedTask, User user) {
        Task existingTask = findOne(id);
        if (!existingTask.getOwner().equals(user)) {
            throw new UnauthorizedException("User is not the owner of this task.");
        }
        existingTask.setHeader(updatedTask.getHeader());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDeadline_timestamp(updatedTask.getDeadline_timestamp());
        return tasksRepository.save(existingTask);
    }

    @Transactional
    public void markDone(long id) {
        Task taskToBeDone = findOne(id);
        taskToBeDone.setDone_timestamp(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );
        tasksRepository.save(taskToBeDone);
    }

    @Transactional
    public void unmarkDone(long id) {
        Task taskToUnmark = findOne(id);
        taskToUnmark.setDone_timestamp(null);
        tasksRepository.save(taskToUnmark);
    }

    @Transactional
    public void deleteById(long id) {
        findOne(id);
        tasksRepository.deleteById(id);
    }

}
