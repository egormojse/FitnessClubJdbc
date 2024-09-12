package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.dao.PersonRepository;
import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PasswordEncoder passwordEncoder; // Добавляем PasswordEncoder

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerPerson(Person person) {
        person.setRole("USER"); // Устанавливаем роль по умолчанию при регистрации
        // Шифруем пароль перед сохранением
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(int id) {
        return Optional.ofNullable(personRepository.findById(id));
    }

    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    public void updatePerson(Person person) {
        personRepository.save(person);
    }
}

