package ru.nasrulaev.tasktrackerbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nasrulaev.tasktrackerbackend.dto.*;
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

    @Operation(summary = "Get user's tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user's tasks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskList.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
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

    @Operation(summary = "Get user's task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user's task",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Task not accessible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO findOne(@AuthenticationPrincipal PersonDetails personDetails,
                           @Parameter(description = "id of the task") @PathVariable(name = "id") long taskId) {
        return convertTaskToDTO(
                tasksService.findOne(taskId, personDetails.getUser())
        );
    }

    @Operation(summary = "Create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskList.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Task already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Empty task header",
                    content = @Content()
            ),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@AuthenticationPrincipal PersonDetails personDetails,
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                      description = "Task to create",
                                      required = true,
                                      content = @Content(
                                              mediaType = "application/json",
                                              schema = @Schema(implementation = CreateTaskRequest.class),
                                              examples = @ExampleObject(
                                                      value = """
                                                              {
                                                                "header": "Make dishes",
                                                                "description": "Make some dishes for dinner",
                                                                "deadline_timestamp": 1732021956"
                                                              }
                                                              """
                                              )
                                      )
                              )
                              @RequestBody @Valid CreateTaskRequest createRequest) {
        Task createdTask = tasksService.save(
                modelMapper.map(
                        createRequest,
                        Task.class
                ),
                personDetails.getUser()
        );

        return convertTaskToDTO(createdTask);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskList.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Task not accessible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Task already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Empty task header",
                    content = @Content()
            ),
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@Parameter(description = "id of the task to be updated") @PathVariable(name = "id") long taskId,
                              @AuthenticationPrincipal PersonDetails personDetails,
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                      description = "Updated task information",
                                      required = true,
                                      content = @Content(
                                              mediaType = "application/json",
                                              schema = @Schema(implementation = UpdateTaskRequest.class),
                                              examples = @ExampleObject(
                                                      value = """
                                                              {
                                                                "header": "Make dishes",
                                                                "description": "Make some dishes for dinner",
                                                                "deadline_timestamp": 1732021956"
                                                              }
                                                              """
                                              )
                                      )
                              )
                              @RequestBody @Valid UpdateTaskRequest updatedTask) {
        Task editedTask = tasksService.update(
                taskId,
                modelMapper.map(
                        updatedTask,
                        Task.class
                ),
                personDetails.getUser()
        );

        return convertTaskToDTO(editedTask);
    }

    @Operation(summary = "Mark task as done")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task marked as done",
                    content = @Content()
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Task not accessible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Task already marked as done",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @PatchMapping("/{id}/mark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markTaskDone(@Parameter(description = "id of the task to be marked as done") @PathVariable(name ="id") long taskId,
                             @AuthenticationPrincipal PersonDetails personDetails) {
        tasksService.markDone(taskId, personDetails.getUser());
    }

    @Operation(summary = "Mark task as not done")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task marked as not done",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskList.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Task not accessible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Task already marked as not done",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
    })
    @PatchMapping("/{id}/unmark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unmarkTaskDone(@Parameter(description = "id of the task to be marked as not done") @PathVariable(name = "id") long taskId,
                               @AuthenticationPrincipal PersonDetails personDetails) {
        tasksService.unmarkDone(taskId, personDetails.getUser());
    }


    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Found user's tasks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskList.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Task not accessible",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Task id not provided",
                    content = @Content()
            ),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@Parameter(description = "id of the task to be deleted") @PathVariable(name = "id") long taskId,
                           @AuthenticationPrincipal PersonDetails personDetails) {
        tasksService.deleteById(taskId, personDetails.getUser());
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
