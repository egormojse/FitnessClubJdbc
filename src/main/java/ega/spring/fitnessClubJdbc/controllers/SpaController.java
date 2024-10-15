package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping("/spa/spa")
    public String spa() {
        return "spa/spa";
    }

    @GetMapping("/spa/spa-employee")
    public String spaEmployee() {
        return "spa/spa-employee";
    }
}
