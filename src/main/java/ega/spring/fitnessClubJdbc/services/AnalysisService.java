package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.dto.PopularProduct;
import ega.spring.fitnessClubJdbc.dto.PopularSpaEmployee;
import ega.spring.fitnessClubJdbc.dto.PopularTime;
import ega.spring.fitnessClubJdbc.dto.PopularTrainer;
import ega.spring.fitnessClubJdbc.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AnalysisService {

    private final WorkoutBookingRepository bookingRepository;
    private final TrainerRepository trainerRepository;
    private final SpaEmployeeRepository spaEmployeeRepository;
    private final OrderRepository orderRepository;
    private final SpaBookingRepository spaBookingRepository;

    @Autowired
    public AnalysisService(WorkoutBookingRepository bookingRepository, TrainerRepository trainerRepository, SpaEmployeeRepository spaEmployeeRepository, OrderRepository orderRepository, SpaBookingRepository spaBookingRepository) {
        this.bookingRepository = bookingRepository;
        this.trainerRepository = trainerRepository;
        this.spaEmployeeRepository = spaEmployeeRepository;
        this.orderRepository = orderRepository;
        this.spaBookingRepository = spaBookingRepository;
    }

    public List<PopularTime> getPopularTimes() {
        return bookingRepository.getPopularTimes();
    }

    public List<PopularTime> getPopularSpaTimes() {
        return spaBookingRepository.getPopularTimes();
    }

    public List<PopularTrainer> getPopularTrainers() {
        return trainerRepository.getPopularTrainers();
    }

    public List<PopularSpaEmployee> getPopularSpaEmployees() {
        return spaEmployeeRepository.getPopularSpaEmployees();
    }

    public List<PopularProduct> getPopularProducts() {
        return orderRepository.getPopularProducts();
    }
}
