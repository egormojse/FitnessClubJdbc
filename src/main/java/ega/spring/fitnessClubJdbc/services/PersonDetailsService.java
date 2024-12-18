package ega.spring.fitnessClubJdbc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.repositories.PersonRepository;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository, JdbcTemplate jdbcTemplate) {
        this.personRepository = personRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Loaded user: " + person.getUsername() + " with password: " + person.getPassword());
        return new PersonDetails(person);
    }



    public Person getUserById(int userId) {
        String sql = "SELECT id, username,first_name, email, password, role FROM person WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setUsername(rs.getString("username"));
            person.setFirst_name(rs.getString("first_name"));
            person.setEmail(rs.getString("email"));
            person.setPassword(rs.getString("password"));
            person.setRole(rs.getString("role"));
            return person;
        }, userId);
    }

    public List<Person> findAll() {
        // Пытаемся получить кешированные данные из Redis
        String cachedPersonsJson = (String) redisTemplate.opsForValue().get("allPersons");

        // Проверка на null, если данных нет в кеше
        if (cachedPersonsJson != null) {
            // Если данные есть в кеше, преобразуем их из JSON (если кешированы в JSON)
            return deserializePersons(cachedPersonsJson);
        }

        // Если данных нет в кеше, выполняем запрос к базе данных
        String sql = "SELECT * FROM person WHERE deleted = false";
        List<Person> persons = jdbcTemplate.query(sql, personRowMapper());

        // Кешируем полученные данные в Redis (сохраняем как строку JSON)
        redisTemplate.opsForValue().set("allPersons", serializePersons(persons));

        return persons;
    }

    // Метод для сериализации списка людей в JSON
    private String serializePersons(List<Person> persons) {
        // Преобразуем список людей в строку JSON (можно использовать библиотеку, например, Jackson)
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(persons);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для десериализации строки JSON в список людей
    private List<Person> deserializePersons(String cachedPersonsJson) {
        // Преобразуем строку JSON обратно в список объектов Person
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(cachedPersonsJson, new TypeReference<List<Person>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deleteUserById(int id) {
        String sql = "UPDATE person SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateUser(int id, Person updatedUser) {
        String sql = "UPDATE person SET first_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatedUser.getFirst_name(), id);
    }

    private RowMapper<Person> personRowMapper() {
        return (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setUsername(rs.getString("username"));
            person.setPassword(rs.getString("password"));
            person.setFirst_name(rs.getString("first_name"));
            person.setEmail(rs.getString("email"));
            person.setDeleted(rs.getBoolean("deleted"));
            person.setRole(rs.getString("role"));
            return person;
        };
    }

}
