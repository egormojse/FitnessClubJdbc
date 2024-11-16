package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class WorkoutBookingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkoutBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GymBooking> findByTrainerIdAndDate(int trainerId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT * FROM workout_booking WHERE trainer_id = ? AND date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{trainerId, startOfDay, endOfDay}, gymBookingRowMapper());
    }

    public List<GymBooking> findByTrainerIdAndDate(int trainerId, LocalDateTime date) {
        String sql = "SELECT * FROM workout_booking WHERE trainer_id = ? AND date = ?";
        return jdbcTemplate.query(sql, new Object[]{trainerId, date}, gymBookingRowMapper());
    }

    public List<GymBooking> findByUserId(int userId) {
        String sql = "SELECT * FROM workout_booking WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, gymBookingRowMapper());
    }

    public GymBooking findById(int id) {
        String sql = "SELECT * FROM workout_booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, gymBookingRowMapper());
    }

    public List<GymBooking> findAllByDeletedFalse() {
        String sql = "SELECT * FROM workout_booking WHERE deleted = false";
        return jdbcTemplate.query(sql, gymBookingRowMapper());
    }

    private RowMapper<GymBooking> gymBookingRowMapper() {
        return (rs, rowNum) -> {
            GymBooking booking = new GymBooking();
            booking.setId(rs.getInt("id"));
            booking.setDate(rs.getDate("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
            booking.setUser(new Person());
            booking.getUser().setId(rs.getInt("user_id"));
            booking.setTrainer(new Trainer());
            booking.getTrainer().setId(rs.getInt("trainer_id"));
            booking.setDeleted(rs.getBoolean("deleted"));
            return booking;
        };
    }
}
