package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Timestamp;
import java.util.Date;

public class TaskDTO {

    @NotEmpty(message = "Header must not be empty")
    private String header;

    private String description;

    private Timestamp deadline_timestamp;

    @PastOrPresent
    private Timestamp done_timestamp;

    public TaskDTO() {
    }

    public TaskDTO(String header, String description, Timestamp deadline_timestamp, Timestamp done_timestamp) {
        this.header = header;
        this.description = description;
        this.deadline_timestamp = deadline_timestamp;
        this.done_timestamp = done_timestamp;
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


    public Date getDeadline_timestamp() {
        return deadline_timestamp;
    }

    public void setDeadline_timestamp(Timestamp deadline_timestamp) {
        this.deadline_timestamp = deadline_timestamp;
    }

    public Date getDone_timestamp() {
        return done_timestamp;
    }

    public void setDone_timestamp(Timestamp done_timestamp) {
        this.done_timestamp = done_timestamp;
    }
}
