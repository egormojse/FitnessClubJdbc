package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.MembershipType;
import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipService {

    private final JdbcTemplate jdbcTemplate;

    public MembershipService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void purchaseMembership(String username, int membershipTypeId) {
        String sqlUser = "SELECT id, username FROM person WHERE username = ?";
        Person person = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> {
            Person p = new Person();
            p.setId(rs.getInt("id"));
            p.setUsername(rs.getString("username"));
            return p;
        }, username);

        if (person == null) {
            throw new IllegalArgumentException("Пользователь с именем " + username + " не найден");
        }

        String sqlMembership = "SELECT type, duration, gym_visits, spa_visits FROM membership_type WHERE id = ?";
        MembershipType membershipType = jdbcTemplate.queryForObject(sqlMembership, (rs, rowNum) -> {
            MembershipType type = new MembershipType();
            type.setType(rs.getString("type"));
            type.setDuration(rs.getInt("duration"));
            type.setGymVisits(rs.getInt("gym_visits"));
            type.setSpaVisits(rs.getInt("spa_visits"));
            return type;
        }, membershipTypeId);

        if (membershipType == null) {
            throw new IllegalArgumentException("Абонемент с данным ID не найден");
        }

        String sqlInsert = "INSERT INTO person_membership (person_id, membership_id, start_date, end_date, remaining_gym_visits, remaining_spa_visits) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, person.getId(), membershipTypeId, LocalDate.now(), LocalDate.now().plusDays(membershipType.getDuration()), membershipType.getGymVisits(), membershipType.getSpaVisits());
    }
}
