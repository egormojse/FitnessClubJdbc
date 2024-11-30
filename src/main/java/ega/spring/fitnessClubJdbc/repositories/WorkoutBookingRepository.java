package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.dto.PopularTime;
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


    public List<GymBooking> findByUserId(int userId) {
        String sql = "SELECT * FROM workout_booking WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, gymBookingRowMapper());
    }

    public GymBooking findById(int id) {
        String sql = "SELECT * FROM workout_booking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, gymBookingRowMapper());
    }

    public List<PopularTime> getPopularTimes() {
        String sql = "SELECT time, COUNT(*) AS count FROM workout_booking GROUP BY time ORDER BY count DESC LIMIT 3";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PopularTime(rs.getString("time"), rs.getInt("count")));
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
