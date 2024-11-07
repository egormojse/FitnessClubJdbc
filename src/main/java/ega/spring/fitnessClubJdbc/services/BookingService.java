package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.Trainer;
import ega.spring.fitnessClubJdbc.repositories.WorkoutBookingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final WorkoutBookingRepository workoutBookingRepository;
    private final JdbcTemplate jdbcTemplate;

    public BookingService(WorkoutBookingRepository workoutBookingRepository, JdbcTemplate jdbcTemplate) {
        this.workoutBookingRepository = workoutBookingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(GymBooking session) {
        String sql = "INSERT INTO workout_booking (trainer_id, user_id, date, status) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                session.getTrainer().getId(),
                session.getUser().getId(),
                session.getDate(),
                session.getStatus());
    }

    public List<String> getOccupiedTimes(int trainerId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        String sql = "SELECT date FROM workout_booking WHERE trainer_id = ? AND date BETWEEN ? AND ? AND deleted = false";

        List<LocalDateTime> bookings = jdbcTemplate.query(sql, new Object[]{trainerId, startOfDay, endOfDay},
                (rs, rowNum) -> rs.getTimestamp("date").toLocalDateTime());

        return bookings.stream()
                .map(booking -> booking.toLocalTime().toString())
                .collect(Collectors.toList());
    }

    public boolean isTimeOccupied(int trainerId, LocalDate trainingDate, String trainingTime) {
        LocalDateTime dateTime = LocalDateTime.of(trainingDate, LocalTime.parse(trainingTime));

        String sql = "SELECT COUNT(*) FROM workout_booking WHERE trainer_id = ? AND date = ? AND deleted = false";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{trainerId, dateTime}, Integer.class);

        return count != null && count > 0;
    }

    public List<GymBooking> getAllBookings() {
        String sql = "SELECT * FROM workout_booking WHERE deleted = false";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            GymBooking booking = new GymBooking();
            booking.setId(rs.getInt("id"));
            booking.setTrainer(new Trainer());
            booking.getTrainer().setId(rs.getInt("trainer_id"));
            booking.setUser(new Person());
            booking.getUser().setId(rs.getInt("user_id"));

            booking.setDate(rs.getTimestamp("date").toLocalDateTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }

    public void deleteBookingById(int id) {
        String sql = "UPDATE workout_booking SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateBooking(int id, GymBooking updatedBooking) {
        String sql = "UPDATE workout_booking SET status = ?, date = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatedBooking.getStatus(), updatedBooking.getDate(), id);
    }

    public GymBooking getBookingById(int bookingId) {
        String sql = "SELECT * FROM workout_booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{bookingId}, (rs, rowNum) -> {
            GymBooking booking = new GymBooking();
            booking.setId(rs.getInt("id"));
            booking.setTrainer(new Trainer());
            booking.getTrainer().setId(rs.getInt("trainer_id"));
            booking.setUser(new Person());
            booking.getUser().setId(rs.getInt("user_id"));
            booking.setDate(rs.getTimestamp("date").toLocalDateTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }

    public List<GymBooking> getUserWorkouts(int userId) {
        String sql = "SELECT * FROM workout_booking WHERE user_id = ? AND deleted = false";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            GymBooking booking = new GymBooking();
            booking.setId(rs.getInt("id"));
            booking.setTrainer(new Trainer());
            booking.getTrainer().setId(rs.getInt("trainer_id"));
            booking.setUser(new Person());
            booking.getUser().setId(rs.getInt("user_id"));
            booking.setDate(rs.getTimestamp("date").toLocalDateTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }

}
