package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.GymBooking;
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
        workoutBookingRepository.save(session);
    }

    public List<String> getOccupiedTimes(int trainerId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<LocalDateTime> bookings = workoutBookingRepository.getOccupiedTimes(trainerId, startOfDay, endOfDay);

        return bookings.stream()
                .map(booking -> booking.toLocalTime().toString())
                .collect(Collectors.toList());
    }

    public boolean isTimeOccupied(int trainerId, LocalDate trainingDate, String trainingTime) {
        LocalDateTime dateTime = LocalDateTime.of(trainingDate, LocalTime.parse(trainingTime));

        LocalDateTime startOfDay = trainingDate.atStartOfDay();
        LocalDateTime endOfDay = trainingDate.atTime(23, 59, 59);

        List<LocalDateTime> occupiedTimes = workoutBookingRepository.getOccupiedTimes(trainerId, startOfDay, endOfDay);

        return occupiedTimes.contains(trainingTime);
    }

}
