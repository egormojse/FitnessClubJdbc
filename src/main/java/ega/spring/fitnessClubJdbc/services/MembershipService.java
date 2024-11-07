package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.MembershipType;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.PersonMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MembershipService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MembershipService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void purchaseMembership(String username, int membershipTypeId) {
        Person person = getPersonByUsername(username);
        MembershipType membershipType = getMembershipTypeById(membershipTypeId);

        PersonMembership existingMembership = findActiveMembershipByPersonId(person.getId());
        if (existingMembership != null) {
            if (existingMembership.getEndDate().isAfter(LocalDate.now())) {
                existingMembership.setRemainingGymVisits(existingMembership.getRemainingGymVisits() + membershipType.getGymVisits());
                existingMembership.setRemainingSpaVisits(existingMembership.getRemainingSpaVisits() + membershipType.getSpaVisits());
            } else {
                existingMembership.setStartDate(LocalDate.now());
                existingMembership.setEndDate(LocalDate.now().plusDays(membershipType.getDuration()));
                existingMembership.setRemainingGymVisits(membershipType.getGymVisits());
                existingMembership.setRemainingSpaVisits(membershipType.getSpaVisits());
            }
            updateMembership(existingMembership);
        } else {
            PersonMembership personMembership = new PersonMembership();
            personMembership.setPerson(person);
            personMembership.setMembershipType(membershipType);
            personMembership.setStartDate(LocalDate.now());
            personMembership.setEndDate(LocalDate.now().plusDays(membershipType.getDuration()));
            personMembership.setRemainingGymVisits(membershipType.getGymVisits());
            personMembership.setRemainingSpaVisits(membershipType.getSpaVisits());

            saveMembership(personMembership);
        }
    }

    private Person getPersonByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setUsername(rs.getString("username"));
            // Установите другие поля, если они есть
            return person;
        });
    }

    private MembershipType getMembershipTypeById(int membershipTypeId) {
        String sql = "SELECT * FROM membership_type WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{membershipTypeId}, (rs, rowNum) -> {
            MembershipType membershipType = new MembershipType();
            membershipType.setId(rs.getInt("id"));
            membershipType.setGymVisits(rs.getInt("gym_visits"));
            membershipType.setSpaVisits(rs.getInt("spa_visits"));
            membershipType.setDuration(rs.getInt("duration"));
            // Установите другие поля, если они есть
            return membershipType;
        });
    }

    private PersonMembership findActiveMembershipByPersonId(int personId) {
        String sql = "SELECT * FROM person_membership WHERE person_id = ? AND end_date > ? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{personId, LocalDate.now()}, (rs, rowNum) -> {
            PersonMembership membership = new PersonMembership();
            membership.setId(rs.getInt("id"));
            membership.setPerson(new Person());
            membership.getPerson().setId(rs.getInt("user_id"));
            membership.setStartDate(rs.getDate("start_date").toLocalDate());
            membership.setEndDate(rs.getDate("end_date").toLocalDate());
            membership.setRemainingGymVisits(rs.getInt("remaining_gym_visits"));
            membership.setRemainingSpaVisits(rs.getInt("remaining_spa_visits"));
            // Установите другие поля, если они есть
            return membership;
        });
    }

    private void updateMembership(PersonMembership membership) {
        String sql = "UPDATE person_membership SET start_date = ?, end_date = ?, remaining_gym_visits = ?, remaining_spa_visits = ? WHERE id = ?";
        jdbcTemplate.update(sql, membership.getStartDate(), membership.getEndDate(), membership.getRemainingGymVisits(), membership.getRemainingSpaVisits(), membership.getId());
    }

    private void saveMembership(PersonMembership membership) {
        String sql = "INSERT INTO person_membership (person_id, membership_id, start_date, end_date, remaining_gym_visits, remaining_spa_visits) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, membership.getPerson().getId(), membership.getMembershipType().getId(), membership.getStartDate(), membership.getEndDate(), membership.getRemainingGymVisits(), membership.getRemainingSpaVisits());
    }
}
