package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.SpaBooking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpaBookingRowMapper implements RowMapper<SpaBooking> {
    @Override
    public SpaBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpaBooking spaBooking = new SpaBooking();
        spaBooking.setId(rs.getInt("id"));
        return spaBooking;
    }
}
