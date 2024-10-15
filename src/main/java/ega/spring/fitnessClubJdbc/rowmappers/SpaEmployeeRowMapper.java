package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpaEmployeeRowMapper implements RowMapper<SpaEmployee> {
    @Override
    public SpaEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpaEmployee spaEmployee = new SpaEmployee();
        spaEmployee.setId(rs.getInt("id"));
        spaEmployee.setName(rs.getString("name"));
        spaEmployee.setSpecialization(rs.getString("specialization"));
        return spaEmployee;
    }
}
