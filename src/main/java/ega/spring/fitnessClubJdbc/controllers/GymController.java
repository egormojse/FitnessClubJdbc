package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GymController {

    @GetMapping("/gym/trainers")
    public String trainers() {
        return "gym/trainers";
    }
}
