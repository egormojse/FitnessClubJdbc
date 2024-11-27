package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.dto.PopularTime;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.SpaBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SpaBookingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpaBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteById(int bookingId) {
        String sql = "DELETE FROM spa_booking WHERE id = ?";
        jdbcTemplate.update(sql, bookingId);
    }

    public void save(SpaBooking spaBooking) {
        String sql = """
            INSERT INTO spa_booking (id, user_id, procedure_id, date, time, status)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO UPDATE
            SET user_id = ?, procedure_id = ?, date = ?, time = ?, status = ?;
        """;
        jdbcTemplate.update(sql,
                spaBooking.getId(),
                spaBooking.getUser().getId(),
                spaBooking.getProcedure().getId(),
                spaBooking.getDate(),
                spaBooking.getTime(),
                spaBooking.getStatus(),
                spaBooking.getUser().getId(),
                spaBooking.getProcedure().getId(),
                spaBooking.getDate(),
                spaBooking.getTime(),
                spaBooking.getStatus()
        );
    }

    private RowMapper<SpaBooking> spaBookingRowMapper() {
        return (rs, rowNum) -> {
            SpaBooking spaBooking = new SpaBooking();
            spaBooking.setId(rs.getInt("id"));
            spaBooking.setEmployeeId(rs.getInt("employee_id"));
            spaBooking.setUser(new Person());
            spaBooking.getUser().setId(rs.getInt("user_id"));
            spaBooking.setDate(rs.getTimestamp("date"));
            spaBooking.setStatus(rs.getString("status"));
            spaBooking.setTime(rs.getTime("time").toLocalTime());
            return spaBooking;
        };
    }

    public List<SpaBooking> findAll() {
        String sql = "SELECT * FROM spa_booking Where deleted=false";
        return jdbcTemplate.query(sql, spaBookingRowMapper());
    }

    public SpaBooking getBookingById(int bookingId) {
        String sql = "SELECT * FROM spa_booking WHERE id = ? and deleted=false";
        return jdbcTemplate.queryForObject(sql, spaBookingRowMapper(), bookingId);
    }

    public List<PopularTime> getPopularTimes() {
        String sql = "SELECT time, COUNT(*) AS count FROM workout_booking GROUP BY time ORDER BY count DESC LIMIT 3";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PopularTime(rs.getString("time"), rs.getInt("count")));
    }
}
