package fr.brilarisoft.paymybuddy.unitaires.controller;

import fr.brilarisoft.paymybuddy.controllers.UsersController;
import fr.brilarisoft.paymybuddy.exceptions.InvalidInputException;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.models.dto.UserDTO;
import fr.brilarisoft.paymybuddy.repository.UsersRepo;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;



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
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UsersController.class)
public class UserControllerTest {
    @MockBean
    private UsersServiceImpl userService;

    @Mock
    private UsersRepo usersRepo;

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

        List<OperationDTO> operations = new ArrayList();
        OperationDTO operation = new OperationDTO(2L, "User", "Desc", 100F, LocalDateTime.now());
        operations.add(operation);

        List<UserDTO> contacts = new ArrayList<>();
        UserDTO userDTO = new UserDTO(1L, "email@email.com");
        contacts.add(userDTO);

        //When
        when(userService.getUserByEmail(anyString())).thenReturn(java.util.Optional.of(newUser));
        when(userService.getListContact(any())).thenReturn(contacts);
        when(userService.getListOperation(any())).thenReturn(operations);
        //Then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attribute("utilisateur", newUser))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contacts"))
                .andExpect(MockMvcResultMatchers.model().attribute("contacts", contacts))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operation"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operations"))
                .andExpect(MockMvcResultMatchers.model().attribute("operations", operations));
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
        when(userService.doPayement(any(Operation.class), any(Long.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                    .flashAttr("operation", new Operation())
                    .flashAttr("user", new User()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    public void loginPageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
