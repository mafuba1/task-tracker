package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;

public class UpdateTaskRequest {

    @NotBlank
    private String header;

    private String description;

    @FutureOrPresent
    private Timestamp deadline_timestamp;

    public UpdateTaskRequest() {
    }

    public UpdateTaskRequest(String header, String description, Timestamp deadline_timestamp) {
        this.header = header;
        this.description = description;
        this.deadline_timestamp = deadline_timestamp;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeadline_timestamp() {
        return deadline_timestamp;
    }

    public void setDeadline_timestamp(Timestamp deadline_timestamp) {
        this.deadline_timestamp = deadline_timestamp;
    }
}
