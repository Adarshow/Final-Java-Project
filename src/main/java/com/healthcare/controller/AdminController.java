package com.healthcare.controller;

import com.healthcare.entity.User;
import com.healthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Admin Profile Management
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(auth.getName());
        model.addAttribute("user", currentUser);
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("newUsername") String newUsername,
                              @RequestParam("newPassword") String newPassword,
                              @RequestParam("confirmPassword") String confirmPassword,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(auth.getName());
        
        // Check if credentials are being changed
        boolean usernameChanged = newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(currentUser.getUsername());
        boolean passwordChanged = newPassword != null && !newPassword.trim().isEmpty();
        
        // Validation
        if (usernameChanged) {
            if (userService.existsByUsername(newUsername)) {
                redirectAttributes.addFlashAttribute("error", "Username already exists!");
                return "redirect:/admin/profile";
            }
        }
        
        if (passwordChanged) {
            if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/admin/profile";
            }
            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long!");
                return "redirect:/admin/profile";
            }
        }
        
        try {
            userService.updateProfile(currentUser, newUsername, newPassword);
            
            // If credentials were changed, logout and redirect to login page
            if (usernameChanged || passwordChanged) {
                // Logout the current user
                new SecurityContextLogoutHandler().logout(request, response, auth);
                
                // Add success message for login page
                redirectAttributes.addFlashAttribute("success", "Profile updated successfully! Please login with your new credentials.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
                return "redirect:/admin/profile";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/admin/profile";
        }
    }

    // User Management
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        
        // Add role counts to avoid complex Thymeleaf expressions
        long adminCount = allUsers.stream().filter(u -> u.getRole() == User.Role.ADMIN).count();
        long doctorCount = allUsers.stream().filter(u -> u.getRole() == User.Role.DOCTOR).count();
        long staffCount = allUsers.stream().filter(u -> u.getRole() == User.Role.STAFF).count();
        long patientCount = allUsers.stream().filter(u -> u.getRole() == User.Role.PATIENT).count();
        
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("doctorCount", doctorCount);
        model.addAttribute("staffCount", staffCount);
        model.addAttribute("patientCount", patientCount);
        
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("email") String email,
                           @RequestParam("role") User.Role role,
                           RedirectAttributes redirectAttributes) {
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Username is required!");
            return "redirect:/admin/users/new";
        }
        
        if (userService.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/admin/users/new";
        }
        
        if (email != null && !email.trim().isEmpty() && userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            return "redirect:/admin/users/new";
        }
        
        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password is required!");
            return "redirect:/admin/users/new";
        }
        
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/admin/users/new";
        }
        
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long!");
            return "redirect:/admin/users/new";
        }
        
        try {
            userService.createUser(username, password, fullName, email, role);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
            return "redirect:/admin/users/new";
        }
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", User.Role.values());
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id,
                           @RequestParam("username") String username,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("email") String email,
                           @RequestParam("role") User.Role role,
                           @RequestParam("enabled") boolean enabled,
                           RedirectAttributes redirectAttributes) {
        
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found!");
            return "redirect:/admin/users";
        }
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Username is required!");
            return "redirect:/admin/users/" + id + "/edit";
        }
        
        if (!username.equals(user.getUsername()) && userService.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            return "redirect:/admin/users/" + id + "/edit";
        }
        
        if (email != null && !email.trim().isEmpty() && !email.equals(user.getEmail()) && userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            return "redirect:/admin/users/" + id + "/edit";
        }
        
        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/admin/users/" + id + "/edit";
            }
            if (password.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long!");
                return "redirect:/admin/users/" + id + "/edit";
            }
        }
        
        try {
            user.setUsername(username);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setRole(role);
            user.setEnabled(enabled);
            
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(password); // UserService will encode it
            }
            
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.findByUsername(auth.getName());
            
            if (currentUser.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "You cannot delete your own account!");
                return "redirect:/admin/users";
            }
            
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "User not found!");
                return "redirect:/admin/users";
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.findByUsername(auth.getName());
            
            if (currentUser.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "You cannot disable your own account!");
                return "redirect:/admin/users";
            }
            
            user.setEnabled(!user.isEnabled());
            userService.save(user);
            
            String status = user.isEnabled() ? "enabled" : "disabled";
            redirectAttributes.addFlashAttribute("success", "User " + status + " successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
