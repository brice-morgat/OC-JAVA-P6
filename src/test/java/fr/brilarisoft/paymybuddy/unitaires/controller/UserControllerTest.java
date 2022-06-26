package fr.brilarisoft.paymybuddy.unitaires.controller;

import fr.brilarisoft.paymybuddy.controllers.UsersController;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(UsersController.class)
public class UserControllerTest {
    @MockBean
    private UsersServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("brice.morgat@gmx.Fr")
    public void premierControllerTest() throws Exception {
//        when(userService.getUserByEmail(anyString())).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void loginPageControllerTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
