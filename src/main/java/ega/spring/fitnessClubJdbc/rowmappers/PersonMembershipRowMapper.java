package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.PersonMembership;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMembershipRowMapper implements RowMapper<PersonMembership> {
    @Override
    public PersonMembership mapRow(ResultSet rs, int rowNum) throws SQLException {
        PersonMembership personMembership = new PersonMembership();
        personMembership.setId(rs.getInt("id"));
        return personMembership;
    }
}
