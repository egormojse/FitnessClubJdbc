package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.SpaProcedure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpaProcedureRowMapper implements RowMapper<SpaProcedure> {
    @Override
    public SpaProcedure mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpaProcedure spaProcedure = new SpaProcedure();
        spaProcedure.setId(rs.getInt("id"));
        spaProcedure.setName(rs.getString("name"));
        spaProcedure.setType(rs.getString("type"));
        spaProcedure.setDuration(rs.getInt("duration"));
        return spaProcedure;
    }
}
