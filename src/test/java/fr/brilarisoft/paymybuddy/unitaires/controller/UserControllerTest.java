package fr.brilarisoft.paymybuddy.unitaires.controller;

import fr.brilarisoft.paymybuddy.controllers.UsersController;
import fr.brilarisoft.paymybuddy.exceptions.InvalidInputException;
import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//Finir de tester les controller
//        Séquence dans le MPD
//
//        Diagramme DTO + Refonte des cases Model
//
//        Pagination
//
//        Script SQL enlevé les éléments inutiles
//        Ajouter les fees
//        Tester les données (plus pertinent)
@WebMvcTest(UsersController.class)
public class UserControllerTest {
    @MockBean
    private UsersServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("brice.morgat@gmx.Fr")
    public void indexPageTest() throws Exception {
        //Given
        User newUser = new User();
        newUser.setSurname("Ashley");
        newUser.setEmail("ashley@gmail.com");
        newUser.setPassword("Password");
        newUser.setBalance(100F);
        when(userService.getUserByEmail(anyString())).thenReturn(java.util.Optional.of(newUser));
        when(userService.getListContact(new HashSet<>())).thenReturn(null);
        when(userService.getListOperation(new HashSet<>())).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contacts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operation"))
                    .andExpect(MockMvcResultMatchers.model().attributeExists("operations"));
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void indexPageErrorTest() throws Exception {
        when(userService.getUserByEmail(anyString())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void makeTransactionTest() throws Exception {
        when(userService.doPayement(any(Operation.class), any(User.class))).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                    .flashAttr("operation", new Operation())
                    .flashAttr("user", new User()))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operation"));
    }

    @Test
    public void loginPageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
