package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.Trainer;
import ega.spring.fitnessClubJdbc.repositories.WorkoutBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final WorkoutBookingRepository workoutBookingRepository;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public BookingService(WorkoutBookingRepository workoutBookingRepository, JdbcTemplate jdbcTemplate) {
        this.workoutBookingRepository = workoutBookingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(GymBooking session) {
        String sql = "INSERT INTO workout_booking (trainer_id, user_id, date, time, status) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                session.getTrainer().getId(),
                session.getUser().getId(),
                session.getDate(),
                session.getTime(),
                session.getStatus());
    }

    public List<String> getOccupiedTimes(int trainerId, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String sql = "SELECT time FROM workout_booking WHERE trainer_id = ? AND date = ? AND deleted = false";

        List<LocalTime> occupiedTimes = jdbcTemplate.query(sql, new Object[]{trainerId, localDate},
                        (rs, rowNum) -> {
                            // Получаем время, если оно не null
                            java.sql.Time sqlTime = rs.getTime("time");
                            return sqlTime != null ? sqlTime.toLocalTime() : null;
                        })
                .stream()
                .filter(Objects::nonNull)  // Убираем null значения
                .collect(Collectors.toList());

        // Если нет забронированных времен, возвращаем пустой список
        if (occupiedTimes.isEmpty()) {
            return Collections.emptyList();
        }

        // Преобразуем в список строк
        return occupiedTimes.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());
    }


    public boolean isTimeOccupied(int trainerId, Date trainingDate, String trainingTime) {
        LocalDate localDate = trainingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalTime time = LocalTime.parse(trainingTime);

        String sql = "SELECT COUNT(*) FROM workout_booking WHERE trainer_id = ? AND date = ? AND time = ? AND deleted = false";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{trainerId, localDate, time}, Integer.class);

        return count != null && count > 0;
    }



    public List<GymBooking> getAllBookings() {

        String sql =  "SELECT wb.id, wb.trainer_id, wb.user_id, wb.date, wb.time, wb.status, " +
                "p.username AS user_username, t.username AS trainer_username " +
                "FROM workout_booking AS wb " +
                "JOIN person p ON wb.user_id = p.id " +
                "JOIN trainers t ON wb.trainer_id = t.id " +
                "WHERE wb.deleted = false";
        List<GymBooking> bookings = jdbcTemplate.query(sql, (rs, rowNum) -> {
            GymBooking booking = new GymBooking();
            booking.setId(rs.getInt("id"));
            Trainer trainer = new Trainer();
            trainer.setUsername(rs.getString("trainer_username"));
            booking.setTrainer(trainer);
            Person person = new Person();
            person.setUsername(rs.getString("user_username"));
            booking.setUser(person);
            booking.getUser().setId(rs.getInt("user_id"));
            booking.setDate(rs.getDate("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });

        return bookings;
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
            booking.setDate(rs.getTimestamp("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
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
            booking.setDate(rs.getTimestamp("date"));
            booking.setTime(rs.getTime("time").toLocalTime());
            booking.setStatus(rs.getString("status"));
            return booking;
        });
    }

}
