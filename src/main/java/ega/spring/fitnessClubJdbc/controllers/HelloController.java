package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.*;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HelloController {


    private final PersonDetailsService personDetailsService;

    public HelloController(PersonDetailsService personDetailsService, BookingService workoutService,
                           OrderService orderService, SpaBookingService spaService, SpaEmployeeService spaEmployeeService, TrainerService trainerService) {
        this.personDetailsService = personDetailsService;
        this.workoutService = workoutService;
        this.orderService = orderService;
        this.spaService = spaService;
        this.spaEmployeeService = spaEmployeeService;
        this.trainerService = trainerService;
    }

    @GetMapping("/FitnessClub")
    public String index() {
        return "index";
    }


    @GetMapping("/purchase")
    public String purchase() {
        return "purchase";
    }

    @GetMapping("/purchaseSpa")
    public String purchaseSpa() {
        return "purchaseSpa";
    }
    private final BookingService workoutService;
    private final OrderService orderService;
    private final SpaBookingService spaService;
    private final SpaEmployeeService spaEmployeeService;
    private final TrainerService trainerService;

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal PersonDetails userDetails) {
        int userId = userDetails.getUserId();
        Person user = personDetailsService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);

        System.out.println("Имя пользователя: " + user.getFirst_name());
        System.out.println("Email пользователя: " + user.getEmail());


        List<GymBooking> workouts = workoutService.getUserWorkouts(userId);
        List<SpaBooking> spaBookings = spaService.getUserSpaBookings(userId);
        List<Order> orders = orderService.getUserOrders(userId);

        for (GymBooking workout : workouts) {
            Trainer trainer = trainerService.getTrainerById(workout.getTrainer().getId());
            workout.getTrainer().setName(trainer.getName());
        }

        for (SpaBooking spaBooking : spaBookings) {
            SpaEmployee employee = spaEmployeeService.getEmployeeById(spaBooking.getEmployeeId());
            spaBooking.setEmployeeName(employee.getName());
        }

        model.addAttribute("workouts", workouts);
        model.addAttribute("spaBookings", spaBookings);
        model.addAttribute("orders", orders);

        return "profile";
    }
}
