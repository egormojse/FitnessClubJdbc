package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.MembershipType;
import ega.spring.fitnessClubJdbc.rowmappers.MembershipTypeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MembershipTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public MembershipTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MembershipType> findAll() {
        String sql = "SELECT * FROM membership_type";
        return jdbcTemplate.query(sql, new MembershipTypeRowMapper());
    }

    public MembershipType findById(int membershipTypeId) {
        String sql = "SELECT * FROM membership_type WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new MembershipTypeRowMapper(), membershipTypeId);
    }
}
