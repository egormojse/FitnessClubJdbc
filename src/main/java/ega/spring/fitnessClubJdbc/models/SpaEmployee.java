package ega.spring.fitnessClubJdbc.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SpaEmployee {

    private int id;

    private String username;

    @Size(min=2, max=50, message = "Имя должно быть больше 2 и меньше 50 символов")
    @NotEmpty(message = "Имя не должно быть пустым")
    private String name;

    private String password;

    @Email(message = "Неверный формат электронной почты")
    private String email;

    private String specialization;

    private int experience;

    private String bio;

    private String role;

    private boolean deleted;

}
