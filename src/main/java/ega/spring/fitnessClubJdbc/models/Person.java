package ega.spring.fitnessClubJdbc.models;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Getter
@Setter
public class Person implements Serializable {
    private int id;

    @Serial
    private static final long serialVersionUID = 123456789L;


    public Person(int id, String username, String first_name, String last_name,
                  Date bd_date, String email, String role, String password) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.bd_date = bd_date;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    @NotEmpty(message = "Имя не должно быть пустым")
    private String username;

    @Size(min=2, max=50, message = "Имя должно быть больше 2 и меньше 50 символов")
    @NotEmpty(message = "Имя не должно быть пустым")
    private String first_name;

    @Size(min=2, max=50, message = "Фамилия должна быть больше 2 и меньше 50 символов")
    @NotEmpty(message = "Фамилия не должна быть пустым")
    private String last_name;

    @NotNull(message = "Введите дату рождения")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date bd_date;

    @Email(message = "Неверный формат электронной почты")
    private String email;

    private String role;

    @NotNull(message = "Введите пароль")
    private String password;

    private boolean deleted;

    public Person() {

    }
}
