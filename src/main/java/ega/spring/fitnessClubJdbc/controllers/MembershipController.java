package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.MembershipType;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.services.MembershipService;
import ega.spring.fitnessClubJdbc.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MembershipController {

    @Autowired
    private MembershipService membershipService;
    private final PersonDetailsService personService;

    public MembershipController(PersonDetailsService personService) {
        this.personService = personService;
    }

    @GetMapping("/purchase/{membershipType}")
    public String purchaseMembership(@PathVariable("membershipType") String membershipType, Authentication authentication) {
        String username = authentication.getName();

        int membershipTypeId = switch (membershipType) {
            case "regular" -> 1; // ID обычного абонемента
            case "student" -> 2; // ID студенческого абонемента
            case "family" -> 3; // ID семейного абонемента
            case "premium" -> 4; // ID премиум абонемента
            case "spa-regular" -> 5; // ID обычного спа абонемента
            case "spa-student" -> 6; // ID студенческого спа абонемента
            case "spa-family" -> 7; // ID семейного спа абонемента
            case "spa-premium" -> 8; // ID премиум спа абонемента
            default -> throw new IllegalArgumentException("Неверный тип абонемента");
        };
        membershipService.purchaseMembership(username, membershipTypeId);
        return "redirect:/FitnessClub";
    }

    @GetMapping("/purchase/constructor")
    public String showConstructorPage(Model model) {
        model.addAttribute("membershipBasePrice", 1.5); // Базовая цена за посещение
        model.addAttribute("membershipBaseDuration", 70); // Базовая цена за месяц
        return "/constructor";
    }

    @PostMapping("/purchase/constructor")
    public String purchaseConstructorMembership(
            @RequestParam int gymVisits,
            @RequestParam int spaVisits,
            @RequestParam int duration,
            Authentication authentication) {

        String username = authentication.getName();
        Person person = personService.getPersonByUsername(username);

        double price = (gymVisits * 1.5) + (spaVisits * 1.5) + (double)(duration / 7 * 20);

        MembershipType constructorType = new MembershipType();
        constructorType.setId(9);
        constructorType.setType("Конструктор");
        constructorType.setGymVisits(gymVisits);
        constructorType.setSpaVisits(spaVisits);
        constructorType.setDuration(duration);
        constructorType.setPrice(price);

        membershipService.createConstructorMembership(person, constructorType);
        return "redirect:/FitnessClub";
    }

}
