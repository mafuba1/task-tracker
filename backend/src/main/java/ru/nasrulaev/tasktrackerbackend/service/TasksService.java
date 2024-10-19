package ru.nasrulaev.tasktrackerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasrulaev.tasktrackerbackend.exception.TaskAlreadyDone;
import ru.nasrulaev.tasktrackerbackend.exception.TaskNotAccessibleException;
import ru.nasrulaev.tasktrackerbackend.exception.TaskNotDoneException;
import ru.nasrulaev.tasktrackerbackend.exception.TaskNotFoundException;
import ru.nasrulaev.tasktrackerbackend.model.Task;
import ru.nasrulaev.tasktrackerbackend.model.User;
import ru.nasrulaev.tasktrackerbackend.repository.TasksRepository;

import java.sql.Timestamp;
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

    public Task findOne(long id, User user) {
        Task task = findOne(id);
        if (!task.getOwner().equals(user)) {
            throw new TaskNotAccessibleException("User is not the owner of this task.");
        }
        return task;
    }

    public List<Task> findTasksByOwner(User owner) {
        return tasksRepository.findTasksByOwner(owner);
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
        Task existingTask = findOne(id, user);

        existingTask.setHeader(updatedTask.getHeader());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDeadline_timestamp(updatedTask.getDeadline_timestamp());

        return tasksRepository.save(existingTask);
    }

    @Transactional
    public void markDone(long id, User user) {
        Task taskToBeDone = findOne(id, user);

        if (taskToBeDone.isDone()) {
            throw new TaskAlreadyDone("Task " + taskToBeDone.getHeader() + " is already done");
        }

        taskToBeDone.setDone_timestamp(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );
        taskToBeDone.setDone(true);
        tasksRepository.save(taskToBeDone);
    }

    @Transactional
    public void unmarkDone(long id, User user) {
        Task taskToUnmark = findOne(id, user);

        if (!taskToUnmark.isDone()) {
            throw new TaskNotDoneException("Task " + taskToUnmark.getHeader() + " is not done");
        }

        taskToUnmark.setDone_timestamp(null);
        taskToUnmark.setDone(false);
        tasksRepository.save(taskToUnmark);
    }

    @Transactional
    public void deleteById(long id, User user) {
        findOne(id, user);
        tasksRepository.deleteById(id);
    }

}
