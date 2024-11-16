package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("id"));
        person.setUsername(rs.getString("username"));
        person.setFirst_name(rs.getString("first_name"));
        person.setLast_name(rs.getString("last_name"));
        person.setBd_date(rs.getDate("bd_date"));
        person.setEmail(rs.getString("email"));
        person.setRole(rs.getString("role"));
        person.setPassword(rs.getString("password"));
        return person;
    }
}
