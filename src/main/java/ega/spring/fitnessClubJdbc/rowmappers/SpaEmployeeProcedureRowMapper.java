package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.SpaEmployeeProcedure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpaEmployeeProcedureRowMapper implements RowMapper<SpaEmployeeProcedure> {
    @Override
    public SpaEmployeeProcedure mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpaEmployeeProcedure spaEmployeeProcedure = new SpaEmployeeProcedure();
        spaEmployeeProcedure.setId(rs.getInt("id"));
        return spaEmployeeProcedure;
    }
}
