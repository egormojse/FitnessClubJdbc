package ega.spring.fitnessClubJdbc.validation;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // Пример проверки: проверка, существует ли уже пользователь с таким же именем
        if (personService.getAllPersons().stream()
                .anyMatch(existingPerson -> existingPerson.getUsername().equals(person.getUsername()))) {
            errors.rejectValue("username", "username.exists", "Имя пользователя уже существует");
        }
    }
}
