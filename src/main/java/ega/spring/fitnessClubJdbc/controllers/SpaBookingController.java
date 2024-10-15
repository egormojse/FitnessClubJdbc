package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.SpaBooking;
import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.models.SpaProcedure;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.PersonDetailsService;
import ega.spring.fitnessClubJdbc.services.SpaBookingService;
import ega.spring.fitnessClubJdbc.services.SpaEmployeeService;
import ega.spring.fitnessClubJdbc.services.SpaProcedureService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spa")
public class SpaBookingController {

    private final SpaEmployeeService spaEmployeeService;
    private final SpaBookingService spaBookingService;
    private final PersonDetailsService personDetailsService;
    private final SpaProcedureService spaProcedureService;

    public SpaBookingController(SpaEmployeeService spaEmployeeService, SpaBookingService spaBookingService,
                                PersonDetailsService personDetailsService, SpaProcedureService spaProcedureService) {
        this.spaEmployeeService = spaEmployeeService;
        this.spaBookingService = spaBookingService;
        this.personDetailsService = personDetailsService;
        this.spaProcedureService = spaProcedureService;
    }

    @GetMapping("/spa-booking")
    public String showSpaForm(Model model, Principal principal) {
        UserDetails userDetails = personDetailsService.loadUserByUsername(principal.getName());
        Person currentUser = ((PersonDetails) userDetails).getPerson();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("employees", spaEmployeeService.getAllEmployees());

        // Retrieve all procedures to show on the booking form
        model.addAttribute("procedures", spaProcedureService.getAllProcedures());

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        model.addAttribute("allTimes", allTimes);
        model.addAttribute("occupiedTimes", List.of());

        return "spa/spa-booking";
    }

    @GetMapping("/getEmployeesAndProceduresByType")
    @ResponseBody
    public Map<String, Object> getEmployeesAndProceduresByType(@RequestParam String type) {
        Map<String, Object> response = new HashMap<>();

        List<SpaEmployee> employees = spaEmployeeService.getEmployeesBySpecialization(type);
        response.put("employees", employees);

        List<SpaProcedure> procedures = spaProcedureService.getProceduresByType(type);
        response.put("procedures", procedures);

        return response;
    }

    @GetMapping("/checkAvailability")
    @ResponseBody
    public Map<String, Object> checkAvailability(@RequestParam int employeeId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<String> occupiedTimes = spaBookingService.getOccupiedTimes(employeeId, date);

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        List<String> availableTimes = allTimes.stream()
                .filter(time -> !occupiedTimes.contains(time))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("availableTimes", availableTimes);
        return response;
    }

    @PostMapping("/submit")
    public String submitWorkout(@RequestParam int employeeId,
                                @RequestParam int procedureId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam String time,
                                @RequestParam int userId,
                                Principal principal,
                                Model model) {
        if (spaBookingService.isTimeOccupied(employeeId, date, time)) {
            model.addAttribute("errorMessage", "Выбранное время занято. Пожалуйста, выберите другое время.");
            return showSpaForm(model, principal);
        }

        LocalDateTime trainingDateTime = LocalDateTime.of(date, LocalTime.parse(time));

        Optional<SpaEmployee> employee = spaEmployeeService.getEmployeeById(employeeId);
        Person user = personDetailsService.getUserById(userId);

        SpaProcedure procedure = spaProcedureService.getProcedureById(procedureId);

        SpaBooking booking = new SpaBooking();
        booking.setEmployeeId(employeeId);
        booking.setUser(user);
        booking.setProcedure(procedure);
        booking.setDate(trainingDateTime);
        booking.setStatus("Зарегистрирован(а)");

        // Save the booking
        spaBookingService.save(booking);
        return "index";
    }

}
