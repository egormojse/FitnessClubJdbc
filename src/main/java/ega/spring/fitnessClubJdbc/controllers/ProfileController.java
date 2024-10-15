/*
package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public String getUserProfile(@PathVariable Long userId, Model model) {
        UserProfileDto profile = userProfileService.getUserProfile(userId);

        model.addAttribute("profile", profile);

        return "profile";
    }


}
*/
