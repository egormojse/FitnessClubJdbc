package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaBookingService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpaBookingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(SpaBooking session) {
        String sql = "INSERT INTO spa_booking (employee_id, date, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, session.getEmployeeId(), session.getDate(), session.getStatus());
    }

    public List<String> getOccupiedTimes(int employeeId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        String sql = "SELECT date FROM spa_booking WHERE employee_id = ? AND date BETWEEN ? AND ?";

        List<LocalDateTime> bookings = jdbcTemplate.query(
                sql,
                new Object[]{employeeId, startOfDay, endOfDay},
                (rs, rowNum) -> rs.getTimestamp("date").toLocalDateTime()
        );

        return bookings.stream()
                .map(booking -> booking.toLocalTime().toString())
                .collect(Collectors.toList());
    }

    public boolean isTimeOccupied(int employeeId, LocalDate date, String time) {
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.parse(time));

        String sql = "SELECT COUNT(*) FROM spa_booking WHERE employee_id = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{employeeId, dateTime}, Integer.class);

        return count != null && count > 0;
    }
}
