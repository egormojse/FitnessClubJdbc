package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.repositories.PersonRepository;
import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PasswordEncoder passwordEncoder;

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerPerson(Person person) {
        person.setRole("USER");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAllByDeletedFalse();
    }
}

