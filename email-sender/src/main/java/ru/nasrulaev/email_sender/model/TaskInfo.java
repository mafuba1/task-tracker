package ru.nasrulaev.email_sender.model;

import java.io.Serializable;
import java.util.Objects;

public class TaskInfo implements Serializable {
    private String header;

    public TaskInfo() {}

    public TaskInfo(String header) {
        this.header = header;
    }

    public void setHeader(String header) {
        this.header = header;
    };

    public String getHeader() {
        return header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInfo entity = (TaskInfo) o;
        return Objects.equals(this.header, entity.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "header = " + header + ")";
    }
}