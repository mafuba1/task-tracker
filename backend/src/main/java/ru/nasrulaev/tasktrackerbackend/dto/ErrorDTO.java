package ru.nasrulaev.tasktrackerbackend.dto;

import java.sql.Timestamp;

public record ErrorDTO(String message, Timestamp timestamp){
}
