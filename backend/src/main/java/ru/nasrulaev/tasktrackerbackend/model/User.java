package ru.nasrulaev.tasktrackerbackend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import ru.nasrulaev.tasktrackerbackend.security.Password;

@Entity
@Table(name = "Users")
public class User {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email
    @Size(max = 320)
    private String email;

    @Column
    @NotBlank(message = "Email is mandatory")
    @Password
    private String password;

    @NotNull
    @ColumnDefault("false")
    private boolean isSubscribed;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
