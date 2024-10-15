package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ega.spring.fitnessClubJdbc.rowmappers.PersonRowMapper;

import java.util.List;

@Repository
public class PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Person person) {
        String sql = "INSERT INTO person (username, first_name, last_name, bd_date, email, role, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getLast_name(), person.getBd_date(),
                person.getEmail(), person.getRole(), person.getPassword());
    }

    public List<Person> findAll() {
        String sql = "SELECT * FROM person";
        return jdbcTemplate.query(sql, new PersonRowMapper());
    }


    public Person findById(int id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new PersonRowMapper(), id);
    }

    public Person findByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";
        List<Person> persons = jdbcTemplate.query(sql, new PersonRowMapper(), username);
        return persons.isEmpty() ? null : persons.get(0);
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM person WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int update(Person person) {
        String sql = "UPDATE person SET username = ?, first_name = ?, last_name = ?," +
                " bd_date = ?, email = ?, role = ?, password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getLast_name(), person.getBd_date(),
                person.getEmail(), person.getRole(), person.getPassword(), person.getId());
    }

}
