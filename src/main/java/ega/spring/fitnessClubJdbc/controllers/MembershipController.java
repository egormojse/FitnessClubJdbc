package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.services.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

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
}
