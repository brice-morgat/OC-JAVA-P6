package fr.brilarisoft.paymybuddy.controllers;

import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.models.dto.UserDTO;
import fr.brilarisoft.paymybuddy.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        Authentication authentication;
        User user;
        List<UserDTO> contacts;
        List<OperationDTO> operations;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userService.getUserByEmail(authentication.getName()).get();
            contacts = userService.getListContact(user.getContacts());
            operations = userService.getListOperation(user.getOperations());
            model.addAttribute("username", authentication.getPrincipal());
            model.addAttribute("utilisateur", user);
            model.addAttribute("contacts", contacts);
            model.addAttribute("operation", new Operation());
            model.addAttribute("operations", operations);
            return "index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/transaction")
    public String makeTransaction(@ModelAttribute Operation operation, @ModelAttribute User user, Model model) {
        try {
            userService.doPayement(operation, user);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}