package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.*;
import ega.spring.fitnessClubJdbc.repositories.PersonMembershipRepository;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spa")
public class SpaBookingController {

    private final SpaEmployeeService spaEmployeeService;
    private final SpaBookingService spaBookingService;
    private final PersonDetailsService personDetailsService;
    private final SpaProcedureService spaProcedureService;
    private final PersonMembershipRepository personMembershipRepository;

    public SpaBookingController(SpaEmployeeService spaEmployeeService, SpaBookingService spaBookingService,
                                PersonDetailsService personDetailsService, SpaProcedureService spaProcedureService, PersonMembershipRepository personMembershipRepository) {
        this.spaEmployeeService = spaEmployeeService;
        this.spaBookingService = spaBookingService;
        this.personDetailsService = personDetailsService;
        this.spaProcedureService = spaProcedureService;
        this.personMembershipRepository = personMembershipRepository;
    }

    @GetMapping("/spa-booking")
    public String showSpaForm(Model model, Principal principal) {
        UserDetails userDetails = personDetailsService.loadUserByUsername(principal.getName());
        Person currentUser = ((PersonDetails) userDetails).getPerson();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("employees", spaEmployeeService.getAllEmployees());

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
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
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
    public String submitSpaBooking(@RequestParam int employeeId,
                                   @RequestParam int procedureId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                   @RequestParam String time,
                                   @RequestParam int userId,
                                   Principal principal,
                                   Model model) {
        // Проверка на занятость времени
        if (spaBookingService.isTimeOccupied(employeeId, date, time)) {
            model.addAttribute("errorMessage", "Выбранное время занято. Пожалуйста, выберите другое время.");
            return showSpaForm(model, principal);
        }

        LocalTime localTime = LocalTime.parse(time);

        SpaEmployee employee = spaEmployeeService.getEmployeeById(employeeId);
        Person user = personDetailsService.getUserById(userId);
        SpaProcedure procedure = spaProcedureService.getProcedureById(procedureId);

        PersonMembership currentMembership = personMembershipRepository.findActiveMembershipByPersonId(user.getId());
        if (currentMembership != null && currentMembership.getRemainingSpaVisits() > 0) {
            currentMembership.setRemainingSpaVisits(currentMembership.getRemainingSpaVisits() - 1);
            personMembershipRepository.updateRemainingSpaVisits(currentMembership.getId(), currentMembership.getRemainingSpaVisits());
        } else {
            model.addAttribute("errorMessage", "У вас недостаточно оставшихся спа-посещений.");
            return showSpaForm(model, principal);
        }

        SpaBooking booking = new SpaBooking();
        booking.setEmployeeId(employeeId);
        booking.setUser(user);
        booking.setProcedure(procedure);
        booking.setDate(date);
        booking.setTime(localTime);
        booking.setStatus("Зарегистрирован(а)");

        spaBookingService.save(booking);
        return "index";
    }


}
