package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.rowmappers.PersonRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Person findByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";
        List<Person> persons = jdbcTemplate.query(sql, new PersonRowMapper(), username);
        return persons.isEmpty() ? null : persons.get(0);
    }



    public Person findById(int id) {
        String sql = "SELECT * FROM person WHERE id = ? AND deleted = false";
        List<Person> persons = jdbcTemplate.query(sql, new Object[]{id}, personRowMapper());
        return persons.isEmpty() ? null : persons.get(0);
    }

    public List<Person> findAllByDeletedFalse() {
        String sql = "SELECT * FROM person WHERE deleted = false";
        return jdbcTemplate.query(sql, personRowMapper());
    }

    public void save(Person person) {
        if (person.getId() == 0) { // Новый пользователь
            String sql = "INSERT INTO person (username, first_name, email, password, deleted) VALUES (?, ?, ?, ?, false)";
            jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getEmail(), person.getPassword());
        } else { // Обновление существующего пользователя
            String sql = "UPDATE person SET username = ?, first_name = ?, email = ?, password = ? WHERE id = ? AND deleted = false";
            jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getEmail(), person.getPassword(), person.getId());
        }
    }


    public void deleteById(int id) {
        String sql = "UPDATE person SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Person> personRowMapper() {
        return (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setUsername(rs.getString("username"));
            person.setFirst_name(rs.getString("first_name"));
            person.setEmail(rs.getString("email"));
            person.setDeleted(rs.getBoolean("deleted"));
            return person;
        };
    }


}
