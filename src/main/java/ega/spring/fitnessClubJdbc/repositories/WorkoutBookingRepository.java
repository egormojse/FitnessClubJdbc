package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.rowmappers.GymBookingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class WorkoutBookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(GymBooking booking) {
        String sql = "INSERT INTO workout_booking (trainer_id, user_id, date, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, booking.getTrainer().getId(), booking.getUser().getId(), booking.getDate(), booking.getStatus());
    }

    public List<GymBooking> findByTrainerIdAndDate(int trainerId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT * FROM workout_booking WHERE trainer_id = ? AND date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new GymBookingRowMapper(), trainerId, startOfDay, endOfDay);
    }

    public boolean isTimeOccupied(int trainerId, LocalDateTime dateTime) {
        String sql = "SELECT COUNT(*) FROM workout_booking WHERE trainer_id = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, trainerId, dateTime);
        return count != null && count > 0;
    }

    public List<LocalDateTime> getOccupiedTimes(int trainerId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT date FROM workout_booking WHERE trainer_id = ? AND date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getTimestamp("date").toLocalDateTime(), trainerId, startOfDay, endOfDay);
    }


}
