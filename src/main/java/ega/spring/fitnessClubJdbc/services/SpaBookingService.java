package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.SpaBooking;
import ega.spring.fitnessClubJdbc.repositories.SpaBookingRepository;
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
    private final SpaBookingRepository spaBookingRepository;

    @Autowired
    public SpaBookingService(JdbcTemplate jdbcTemplate, SpaBookingRepository spaBookingRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.spaBookingRepository = spaBookingRepository;
    }

    public SpaBooking getBookingById(int bookingId) {
        return spaBookingRepository.getBookingById(bookingId);
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

    public List<SpaBooking> getUserSpaBookings(int userId) {
        String sql = "SELECT * FROM spa_booking WHERE user_id = ? AND deleted = false";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            SpaBooking booking = new SpaBooking();
            booking.setId(rs.getInt("id"));
            booking.setEmployeeId(rs.getInt("employee_id"));
            booking.setUser(new Person());
            booking.getUser().setId(rs.getInt("user_id"));
            booking.setDate(rs.getTimestamp("date").toLocalDateTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }

    public List<SpaBooking> getAllBookings() {
        return spaBookingRepository.findAll();
    }

    public void deleteById(int id) {
        spaBookingRepository.deleteById(id);
    }

    public void updateBooking(int bookingId, SpaBooking spaBooking) {
        spaBookingRepository.save(spaBooking);
    }

}
