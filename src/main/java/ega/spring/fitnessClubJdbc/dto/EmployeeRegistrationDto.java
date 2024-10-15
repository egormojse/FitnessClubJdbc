package ega.spring.fitnessClubJdbc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRegistrationDto {

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть больше 2 и меньше 50 символов")
    private String username;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть больше 2 и меньше 50 символов")
    private String name;

    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Неверный формат email")
    private String email;

    @NotEmpty(message = "Специализация не должна быть пустой")
    private String specialization;

    @Min(value = 0, message = "Опыт должен быть не отрицательным")
    private int experience;

    @NotEmpty(message = "Bio не должно быть пустым")
    private String bio;

    private String role;
}
