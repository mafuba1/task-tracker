package ru.nasrulaev.tasktrackerbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Timestamp;

@Entity
@Table(name = "Tasks")
public class Task {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty
    private String header;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @Column
    private Timestamp deadline_timestamp;

    @Column
    @PastOrPresent
    private Timestamp done_timestamp;

    public Task() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Timestamp getDeadline_timestamp() {
        return deadline_timestamp;
    }

    public void setDeadline_timestamp(Timestamp deadline_timestamp) {
        this.deadline_timestamp = deadline_timestamp;
    }

    public Timestamp getDone_timestamp() {
        return done_timestamp;
    }

    public void setDone_timestamp(Timestamp done_timestamp) {
        this.done_timestamp = done_timestamp;
    }
}
