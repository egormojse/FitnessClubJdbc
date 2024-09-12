package ega.spring.fitnessClubJdbc.models;


import ega.spring.fitnessClubJdbc.enums.membershipType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter

public class Person {
    private int id;

    public Person(int id, String username, String first_name, String last_name,
                  LocalDate bd_date, String email, String role, String password,
                  membershipType membershipType) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.bd_date = bd_date;
        this.email = email;
        this.role = role;
        this.password = password;
        this.membershipType = membershipType;
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
    @DateTimeFormat(pattern = "MM-dd-yyyy")  // Указываем формат даты
    private LocalDate bd_date;  // Поле остается типа LocalDate

    @AssertTrue(message = "Возраст должен быть больше 12 лет")
    public boolean isAgeValid() {
        if (bd_date == null) {
            return false;
        }
        return Period.between(bd_date, LocalDate.now()).getYears() >= 12;
    }

    @Email(message = "Неверный формат электронной почты")
    private String email;

    private String role;

    @NotNull(message = "Введите пароль")
    private String password;

    private membershipType membershipType;

    public Person() {

    }




}
