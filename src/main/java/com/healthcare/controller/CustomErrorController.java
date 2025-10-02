package com.healthcare.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("statusCode", 403);
        return "error/403";
    }

    @GetMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("statusCode", 404);
        return "error/404";
    }

    @GetMapping("/error/500")
    public String serverError(Model model) {
        model.addAttribute("statusCode", 500);
        return "error/500";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // Get user info if available
                String userRole = getUserRole(request);
                String requestedUrl = getRequestedUrl(request);
                
                model.addAttribute("userRole", userRole);
                model.addAttribute("requestedUrl", requestedUrl);
                model.addAttribute("statusCode", statusCode);
                
                return "error/403";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("statusCode", statusCode);
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("statusCode", statusCode);
                return "error/500";
            }
        }
        
        return "error/general";
    }
    
    private String getUserRole(HttpServletRequest request) {
        // Try to get user role from security context
        try {
            if (request.getUserPrincipal() != null) {
                // This is a simplified approach - in a real app you'd get this from SecurityContext
                return "Current User";
            }
        } catch (Exception e) {
            // Ignore and return default
        }
        return "Guest";
    }
    
    private String getRequestedUrl(HttpServletRequest request) {
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        return requestUri != null ? requestUri.toString() : "Unknown";
    }
}
