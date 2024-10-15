package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.SpaBooking;
import ega.spring.fitnessClubJdbc.rowmappers.SpaBookingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SpaBookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpaBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SpaBooking> findAll() {
        String sql = "SELECT * FROM spa_booking";
        return jdbcTemplate.query(sql, new SpaBookingRowMapper());
    }

    public SpaBooking findById(int bookingId) {
        String sql = "SELECT * FROM spa_booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new SpaBookingRowMapper(), bookingId);
    }

    public List<SpaBooking> findByEmployeeIdAndDate(int employeeId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT * FROM spa_booking WHERE employee_id = ? AND date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new SpaBookingRowMapper(), employeeId, startOfDay, endOfDay);
    }

    public List<SpaBooking> findByEmployeeIdAndDate(int employeeId, LocalDateTime date) {
        String sql = "SELECT * FROM spa_booking WHERE employee_id = ? AND date = ?";
        return jdbcTemplate.query(sql, new SpaBookingRowMapper(), employeeId, date);
    }
}
