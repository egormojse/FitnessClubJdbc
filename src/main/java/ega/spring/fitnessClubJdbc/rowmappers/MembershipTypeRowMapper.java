package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.MembershipType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipTypeRowMapper implements RowMapper<MembershipType> {
    @Override
    public MembershipType mapRow(ResultSet rs, int rowNum) throws SQLException {
        MembershipType membershipType = new MembershipType();
        membershipType.setId(rs.getInt("id"));
        membershipType.setType(rs.getString("type"));
        membershipType.setDuration(rs.getInt("duration"));
        membershipType.setGymVisits(rs.getInt("gym_visits"));
        membershipType.setSpaVisits(rs.getInt("spa_visits"));
        return membershipType;
    }
}
