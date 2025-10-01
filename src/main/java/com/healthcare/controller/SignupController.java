package com.healthcare.controller;

import com.healthcare.entity.User;
import com.healthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, 
                               String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        
        // Validate password match
        if (!user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addAttribute("error", "Passwords do not match");
            return "redirect:/signup";
        }
        
        try {
            // Check if email already exists
            if (userService.findByEmail(user.getEmail()) != null) {
                redirectAttributes.addAttribute("error", "Email already registered");
                return "redirect:/signup";
            }
            
            // Save the user
            userService.save(user);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/signup";
        }
    }
}