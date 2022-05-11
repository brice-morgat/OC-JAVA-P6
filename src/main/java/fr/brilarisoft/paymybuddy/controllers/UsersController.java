package fr.brilarisoft.paymybuddy.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {
    @GetMapping("/hello")
    public String helloWorld() {
        return "hello";
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getPrincipal());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}