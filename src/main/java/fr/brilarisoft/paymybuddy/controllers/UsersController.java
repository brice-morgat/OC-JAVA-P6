package fr.brilarisoft.paymybuddy.controllers;

import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.models.dto.UserDTO;
import fr.brilarisoft.paymybuddy.services.IUserService;
import org.apache.commons.compress.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UsersController {

    @Autowired
    private IUserService userService;



    @GetMapping("/")
    public String indexPage(Model model) {
        Authentication authentication;
        User user;
        List<UserDTO> contacts;
        List<OperationDTO> operations;

        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userService.getUserByEmail(authentication.getName()).get();
            System.out.println(user);
            Page<Operation> page = userService.findPage(user.getId(),1);
            System.out.println(page);
            int totalPage = page.getTotalPages();

            contacts = userService.getListContact(user.getContacts());
            operations = userService.getListOperation(Set.copyOf(page.getContent()));
            model.addAttribute("username", authentication.getPrincipal());
            model.addAttribute("totalPages", totalPage);
            model.addAttribute("currentPage", 1);
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

    @GetMapping("/page={pageNumber}")
    public String indexOnePage(Model model, @PathVariable("pageNumber") int currentPage) {
        Authentication authentication;
        User user;
        List<UserDTO> contacts;
        List<OperationDTO> operations;


        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userService.getUserByEmail(authentication.getName()).get();

            Page<Operation> page = userService.findPage(user.getId(),currentPage);
            int totalPage = page.getTotalPages();

            contacts = userService.getListContact(user.getContacts());
            operations = userService.getListOperation(Set.copyOf(page.getContent()));
            model.addAttribute("totalPages", totalPage);
            model.addAttribute("currentPage", currentPage);
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
    public String makeTransaction(@ModelAttribute("operation") Operation operation, @ModelAttribute("user") User user, Model model) {
        try {
            userService.doPayement(operation, user.getId());
            return "redirect:/";
        } catch (RuntimeException e ) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}