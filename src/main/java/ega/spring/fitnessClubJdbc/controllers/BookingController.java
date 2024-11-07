package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.GymBooking;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.PersonMembership;
import ega.spring.fitnessClubJdbc.models.Trainer;
import ega.spring.fitnessClubJdbc.repositories.PersonMembershipRepository;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.BookingService;
import ega.spring.fitnessClubJdbc.services.PersonDetailsService;
import ega.spring.fitnessClubJdbc.services.TrainerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gym")
public class BookingController {

    private final TrainerService trainerService;
    private final BookingService workoutBookingService;
    private final PersonDetailsService personDetailsService;
    private final PersonMembershipRepository personMembershipRepository;


    public BookingController(TrainerService trainerService, BookingService workoutBookingService, PersonDetailsService personDetailsService, PersonMembershipRepository personMembershipRepository) {
        this.trainerService = trainerService;
        this.workoutBookingService = workoutBookingService;
        this.personDetailsService = personDetailsService;
        this.personMembershipRepository = personMembershipRepository;
    }

    @GetMapping("/trainer_form")
    public String showTrainerForm(Model model, Principal principal) {
        List<Trainer> trainers = trainerService.getAllTrainers();
        UserDetails userDetails = personDetailsService.loadUserByUsername(principal.getName());
        Person currentUser = ((PersonDetails) userDetails).getPerson();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("trainers", trainers);

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        model.addAttribute("allTimes", allTimes);
        model.addAttribute("occupiedTimes", List.of());

        return "gym/trainer_form";
    }

    @GetMapping("/checkAvailability")
    @ResponseBody
    public Map<String, Object> checkAvailability(@RequestParam int trainerId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate) {
        List<String> occupiedTimes = workoutBookingService.getOccupiedTimes(trainerId, trainingDate);

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        List<String> availableTimes = allTimes.stream()
                .filter(time -> !occupiedTimes.contains(time))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("availableTimes", availableTimes);
        return response;
    }

    @PostMapping("/submitWorkout")
    public String submitWorkoutBooking(@RequestParam int trainerId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate,
                                       @RequestParam String trainingTime,
                                       @RequestParam int userId,
                                       Principal principal,
                                       Model model) {
        if (workoutBookingService.isTimeOccupied(trainerId, trainingDate, trainingTime)) {
            model.addAttribute("errorMessage", "Выбранное время занято. Пожалуйста, выберите другое время.");
            return showTrainerForm(model, principal);
        }

        LocalDateTime trainingDateTime = LocalDateTime.of(trainingDate, LocalTime.parse(trainingTime));
        Trainer trainer = trainerService.getTrainerById(trainerId);
        Person user = personDetailsService.getUserById(userId);

        PersonMembership currentMembership = personMembershipRepository.findActiveMembershipByPersonId(user.getId());
        if (currentMembership != null && currentMembership.getRemainingSpaVisits() > 0) {
            currentMembership.setRemainingSpaVisits(currentMembership.getRemainingSpaVisits() - 1);
            personMembershipRepository.updateRemainingSpaVisits(currentMembership.getId(), currentMembership.getRemainingSpaVisits());
        }
        else {
            model.addAttribute("errorMessage", "У вас недостаточно оставшихся посещений.");
            return showTrainerForm(model, principal);
        }

        GymBooking booking = new GymBooking();
        booking.setTrainer(trainer);
        booking.setUser(user);
        booking.setDate(trainingDateTime);
        booking.setStatus("Зарегистрирован(а)");

        workoutBookingService.save(booking);
        return "index";
    }

}
