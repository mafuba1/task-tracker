package ru.nasrulaev.tasktrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.nasrulaev.tasktrackerbackend.security.Password;

public class AuthenticationRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Password
    private String password;

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthenticationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
