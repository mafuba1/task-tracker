package ru.nasrulaev.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.CreateTaskRequest;
import ru.nasrulaev.tasktrackerbackend.dto.TaskDTO;
import ru.nasrulaev.tasktrackerbackend.dto.TaskList;
import ru.nasrulaev.tasktrackerbackend.model.Task;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;
import ru.nasrulaev.tasktrackerbackend.service.TasksService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final TasksService tasksService;
    private final ModelMapper modelMapper;

    @Autowired
    public TasksController(TasksService tasksService, ModelMapper modelMapper) {
        this.tasksService = tasksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskList findAll(@AuthenticationPrincipal PersonDetails personDetails) {
        List<TaskDTO> tasks = tasksService.findTasksByOwner(
                personDetails.getUser()
        ).stream()
                .map(this::convertTaskToDTO)
                .toList();

        return new TaskList(tasks);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody @Valid CreateTaskRequest createRequest) {
         Task newTask = tasksService.save(
                modelMapper.map(
                        createRequest,
                        Task.class
                )
        );

        return convertTaskToDTO(newTask);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTask(@PathVariable(name = "id") long taskId,
                           @RequestBody @Valid TaskDTO updatedTask) {
        tasksService.update(
                taskId,
                convertDTOtoTask(updatedTask)
        );
    }

    @PatchMapping("/{id}/mark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markTaskDone(@PathVariable(name ="id") long taskId) {
        tasksService.markDone(taskId);
    }

    @PatchMapping("/{id}/unmark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unmarkTaskDone(@PathVariable(name = "id") long taskId) {
        tasksService.unmarkDone(taskId);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable(name = "id") long taskId) {
        tasksService.deleteById(taskId);
    }

    private Task convertDTOtoTask(TaskDTO taskDTO) {
        return modelMapper.map(
                taskDTO,
                Task.class
        );
    }

    private TaskDTO convertTaskToDTO(Task task) {
        return modelMapper.map(
                task,
                TaskDTO.class
        );
    }

}
