package fr.brilarisoft.paymybuddy.controllers;

import fr.brilarisoft.paymybuddy.services.IUserService;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {

    @Autowired
    private IUserService userService;

    @GetMapping("/hello")
    public String helloWorld() {
        return "hello";
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getPrincipal());
        model.addAttribute("allUser", userService.getAll());
        model.addAttribute("utilisateur", userService.getUserByEmail(authentication.getName()).get());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}