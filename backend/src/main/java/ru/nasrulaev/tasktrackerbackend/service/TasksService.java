package ru.nasrulaev.tasktrackerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.exception.TaskNotFoundException;
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
    public void save(Task task) {
        tasksRepository.save(task);
    }

    @Transactional
    public void update(long id, Task updatedTask) {
        findOne(id);
        updatedTask.setId(id);
        tasksRepository.save(updatedTask);
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
