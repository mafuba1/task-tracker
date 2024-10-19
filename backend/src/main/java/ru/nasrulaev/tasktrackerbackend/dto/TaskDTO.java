package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Timestamp;
import java.util.Date;

public class TaskDTO {

    private long id;

    @NotEmpty(message = "Header must not be empty")
    private String header;

    private String description;

    private Timestamp deadline_timestamp;

    private boolean done;

    @PastOrPresent
    private Timestamp done_timestamp;

    public TaskDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDone_timestamp() {
        return done_timestamp;
    }

    public void setDone_timestamp(Timestamp done_timestamp) {
        this.done_timestamp = done_timestamp;
    }
}
