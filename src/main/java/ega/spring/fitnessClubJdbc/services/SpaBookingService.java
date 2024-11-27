package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.SpaBooking;
import ega.spring.fitnessClubJdbc.repositories.SpaBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
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
        String sql = "INSERT INTO spa_booking (user_id, procedure_id, date, time, status, employee_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, session.getUser().getId(), session.getProcedure().getId(), session.getDate(), session.getTime(), session.getStatus(), session.getEmployeeId());
    }

    public List<String> getOccupiedTimes(int employeeId, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String sql = "SELECT time FROM spa_booking WHERE employee_id = ? AND date = ?";

        List<LocalTime> occupiedTimes = jdbcTemplate.query(
                sql,
                new Object[]{employeeId, localDate},
                (rs, rowNum) -> rs.getTime("time").toLocalTime()
        );

        // Преобразуем LocalTime в строку времени
        return occupiedTimes.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());
    }

    public boolean isTimeOccupied(int employeeId, Date date, String time) {
        LocalTime localTime = LocalTime.parse(time);

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String sql = "SELECT COUNT(*) FROM spa_booking WHERE employee_id = ? AND date = ? AND time = ?";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{employeeId, localDate, localTime}, Integer.class);

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
            booking.setDate(rs.getTimestamp("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
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

    public List<SpaBooking> getBookingsForPeriod(String startDate, String endDate) {
        String sql = "SELECT * FROM spa_booking WHERE date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{startDate, endDate}, (rs, rowNum) -> {
            SpaBooking booking = new SpaBooking();
            booking.setId(rs.getInt("id"));
            booking.setDate(rs.getDate("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }
}
