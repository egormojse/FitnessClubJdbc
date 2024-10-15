package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GymBookingRowMapper implements RowMapper<GymBooking> {
    @Override
    public GymBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
        GymBooking gymBooking = new GymBooking();
        gymBooking.setId(rs.getInt("id"));
        return gymBooking;
    }
}
