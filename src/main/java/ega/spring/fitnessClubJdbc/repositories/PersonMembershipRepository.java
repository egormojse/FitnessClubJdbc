package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.PersonMembership;
import ega.spring.fitnessClubJdbc.rowmappers.PersonMembershipRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonMembershipRepository {

    private final JdbcTemplate jdbcTemplate;

    public PersonMembershipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PersonMembership> findAll() {
        String sql = "SELECT * FROM person_membership";
        return jdbcTemplate.query(sql, new PersonMembershipRowMapper());
    }

    public PersonMembership findById(int membershipId) {
        String sql = "SELECT * FROM person_membership WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new PersonMembershipRowMapper(), membershipId);
    }
}
