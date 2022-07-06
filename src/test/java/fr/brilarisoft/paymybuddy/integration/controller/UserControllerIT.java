package fr.brilarisoft.paymybuddy.integration.controller;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.services.IUserService;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;


import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    @BeforeEach
    public void init(){
        executeSql("src/main/resources/paymybuddy_test_db.sql");
    }

    private void executeSql(String sqlFilePath) {
        final class SqlExecuter extends SQLExec {
            public SqlExecuter() {
                Project project = new Project();
                project.init();
                setProject(project);
                setTaskType("sql");
                setTaskName("sql");
            }
        }

        SqlExecuter executer = new SqlExecuter();
        executer.setSrc(new File(sqlFilePath));
        executer.setDriver("com.mysql.jdbc.Driver");
        executer.setPassword(env.getProperty("spring.datasource.password"));
        executer.setUserid(env.getProperty("spring.datasource.username"));
        executer.setUrl(env.getProperty("spring.datasource.url"));
        executer.execute();
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void indexTest() throws Exception {
        //Given
        Set<Contact> contacts = new HashSet<>();
        Contact contact = new Contact();
        contact.setId(4L);
        contact.setContactId(1L);
        contacts.add(contact);

        User user = new User();
        user.setId(4L);
        user.setEmail("brice.morgat@gmx.fr");
        user.setSurname("Brice");
        user.setPassword("$2a$10$UxsKx3mg7JsY8OyB4rHu9ueM0evtvJZUBdrr1xF28Dyfmt1m45fRq");
        user.setBalance(0.0F);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attribute("utilisateur", allOf(
                        hasProperty("email", equalTo("brice.morgat@gmx.fr")),
                        hasProperty("surname", equalTo("Brice")),
                        hasProperty("id", equalTo(4L))
                )))
                .andExpect(MockMvcResultMatchers.model().attributeExists("contacts"))
                .andExpect(MockMvcResultMatchers.model().attribute("contacts", hasItem(allOf(
                        hasProperty("id", equalTo(1L)),
                        hasProperty("email", equalTo("haley@gmail.com"))
                ))))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operation"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("operations"));
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void makeTransactionTest() throws Exception {
        Optional<User> user = userService.getUserByEmail("brice.morgat@gmx.Fr");
        Operation operation = new Operation();
        operation.setAmount(10F);
        operation.setDescription("Description");
        operation.setReceiverId(1L);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .flashAttr("operation", operation)
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void makeTransactionErrorWithoutNotFoundUserTest() throws Exception {
        Optional<User> user = userService.getUserByEmail("brice.morgat@gmx.fr");
        Operation operation = new Operation();
        operation.setAmount(10F);
        operation.setDescription("Description");
        operation.setReceiverId(28L);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .flashAttr("operation", operation)
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser("brice.morgat@gmx.fr")
    public void makeTransactionErrorWithoutEnoughMoneyTest() throws Exception {
        Optional<User> user = userService.getUserByEmail("brice.morgat@gmx.fr");
        Operation operation = new Operation();
        operation.setAmount(1000F);
        operation.setDescription("Description");
        operation.setReceiverId(1L);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .flashAttr("operation", operation)
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    public void loginPageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
