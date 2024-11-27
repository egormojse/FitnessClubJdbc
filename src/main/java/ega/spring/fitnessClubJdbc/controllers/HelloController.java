package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.*;
import ega.spring.fitnessClubJdbc.repositories.PersonMembershipRepository;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class HelloController {


    private final PersonDetailsService personDetailsService;
    private final PersonMembershipRepository personMembershipRepository;


    public HelloController(PersonDetailsService personDetailsService, PersonMembershipRepository personMembershipRepository, BookingService workoutService,
                           OrderService orderService, SpaBookingService spaService, SpaEmployeeService spaEmployeeService, TrainerService trainerService) {
        this.personDetailsService = personDetailsService;
        this.personMembershipRepository = personMembershipRepository;
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

        PersonMembership personMembership = personMembershipRepository.findActiveMembershipByPersonId(userId);
        model.addAttribute("personMembership", personMembership);

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

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal PersonDetails userDetails) {
        try {
            // Получаем текущего пользователя (например, из сессии или контекста)
            int userId = userDetails.getUserId();
            Person currentUser = personDetailsService.getUserById(userId);

            // Обновляем данные пользователя
            currentUser.setFirst_name(name);
            currentUser.setEmail(email);
            personDetailsService.updateUser(userId, currentUser);

            // Добавляем сообщение об успешном обновлении
            redirectAttributes.addFlashAttribute("successMessage", "Профиль успешно обновлен.");
        } catch (Exception e) {
            // Добавляем сообщение об ошибке
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении профиля.");
        }

        // Перенаправляем пользователя обратно на страницу профиля
        return "redirect:/profile";
    }
}
