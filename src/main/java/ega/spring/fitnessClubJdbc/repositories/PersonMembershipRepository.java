package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.PersonMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonMembershipRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonMembershipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PersonMembership findActiveMembershipByPersonId(int personId) {
        String sql = "SELECT * FROM person_membership WHERE person_id = ? AND end_date >= CURRENT_DATE";
        List<PersonMembership> memberships = jdbcTemplate.query(sql, new Object[]{personId}, personMembershipRowMapper());
        return memberships.isEmpty() ? null : memberships.get(0);
    }

    private RowMapper<PersonMembership> personMembershipRowMapper() {
        return (rs, rowNum) -> {
            PersonMembership membership = new PersonMembership();
            membership.setId(rs.getInt("id"));
            membership.setPerson(new Person());
            membership.getPerson().setId(rs.getInt("person_id"));
            membership.setStartDate(rs.getDate("start_date").toLocalDate());
            membership.setEndDate(rs.getDate("end_date").toLocalDate());
            membership.setRemainingGymVisits(rs.getInt("remaining_gym_visits"));
            membership.setRemainingSpaVisits(rs.getInt("remaining_spa_visits"));
            return membership;
        };
    }

    public void updateRemainingSpaVisits(int membershipId, int remainingSpaVisits) {
        String sql = "UPDATE person_membership SET remaining_spa_visits = ? WHERE id = ?";
        jdbcTemplate.update(sql, remainingSpaVisits, membershipId);
    }

}
