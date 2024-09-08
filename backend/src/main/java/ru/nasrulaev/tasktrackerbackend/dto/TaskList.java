package ru.nasrulaev.tasktrackerbackend.dto;

import java.util.List;

public record TaskList(List<TaskDTO> tasks) {
}
