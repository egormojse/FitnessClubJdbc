package ega.spring.fitnessClubJdbc.services;


import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.dao.PersonRepository;
import ega.spring.fitnessClubJdbc.security.PersonDetails;  // Импортируем ваш класс PersonDetails
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Используем ваш класс PersonDetails для создания объекта UserDetails
        return new PersonDetails(person);
    }
}

