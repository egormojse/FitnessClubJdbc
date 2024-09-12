package ega.spring.fitnessClubJdbc.dao;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.enums.membershipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Добавление нового пользователя
    public void save(Person person) {
        String sql = "INSERT INTO person (username, first_name, last_name, bd_date, email, role, password, membershiptype) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getLast_name(), person.getBd_date(),
                person.getEmail(), person.getRole(), person.getPassword(), person.getMembershipType());
    }

    // Получение всех пользователей
    public List<Person> findAll() {
        String sql = "SELECT * FROM person";
        return jdbcTemplate.query(sql, new PersonRowMapper());
    }

    // Поиск пользователя по ID
    public Person findById(int id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new PersonRowMapper(), id);
    }

    // Поиск пользователя по имени пользователя
    public Person findByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";
        List<Person> persons = jdbcTemplate.query(sql, new PersonRowMapper(), username);
        return persons.isEmpty() ? null : persons.get(0);
    }

    // Удаление пользователя
    public void deleteById(int id) {
        String sql = "DELETE FROM person WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Обновление информации о пользователе
    public int update(Person person) {
        String sql = "UPDATE person SET username = ?, first_name = ?, last_name = ?," +
                " bd_date = ?, email = ?, role = ?, password = ?, membershiptype = ? WHERE id = ?";
        return jdbcTemplate.update(sql, person.getUsername(), person.getFirst_name(), person.getLast_name(), person.getBd_date(),
                person.getEmail(), person.getRole(), person.getPassword(), person.getMembershipType().toString(), person.getId());
    }

    // Внутренний класс для маппинга результата запроса на объект Person
    private static class PersonRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setUsername(rs.getString("username"));
            person.setFirst_name(rs.getString("first_name"));
            person.setLast_name(rs.getString("last_name"));
            person.setBd_date(rs.getDate("bd_date").toLocalDate());
            person.setEmail(rs.getString("email"));
            person.setRole(rs.getString("role"));
            person.setPassword(rs.getString("password"));
            person.setMembershipType(membershipType.STANDARD);
            return person;
        }
    }
}
