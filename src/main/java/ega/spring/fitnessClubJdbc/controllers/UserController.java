package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/user/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping("/user/profile-settings")
    public String profileSettings() {
        return "user/profile-settings";
    }
}
