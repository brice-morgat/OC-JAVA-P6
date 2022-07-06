package fr.brilarisoft.paymybuddy.integration.service;

import fr.brilarisoft.paymybuddy.exceptions.InvalidInputException;
import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

    @Autowired
    private UsersServiceImpl userService;

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
    public void saveUserTest() {
        //Given
        User newUser = new User();
        newUser.setSurname("Ashley");
        newUser.setEmail("ashley@gmail.com");
        newUser.setPassword("Password");
        newUser.setBalance(100F);
        //When
        User userAdded = userService.saveUser(newUser);
        //Then
        assertEquals(userAdded.getSurname(), userAdded.getSurname());
        assertEquals(userAdded.getEmail(), userAdded.getEmail());
    }

    @Test
    public void saveUserInvalidParamTest() {
        //Given
        User expected = new User();
        expected.setEmail(null);
        expected.setPassword(null);
        expected.setBalance(null);
        //Then
        assertThrows(InvalidInputException.class, () -> userService.saveUser(expected));
    }

    @Test
    public void saveUserInvalidParamWithoutEmailTest() {
        //Given
        User expected = new User();
        expected.setEmail(null);
        expected.setPassword("motdepasse");
        expected.setBalance(100F);
        //Then
        assertThrows(InvalidInputException.class, () -> userService.saveUser(expected));
    }

    @Test
    public void saveUserInvalidParamWithoutBalanceTest() {
        //Given
        User expected = new User();
        expected.setEmail("email");
        expected.setPassword("motdepasse");
        expected.setBalance(null);
        //Then
        assertThrows(InvalidInputException.class, () -> userService.saveUser(expected));
    }

    @Test
    public void doPaymentTest() {
        //Given
        Operation operation = new Operation();
        operation.setAmount(100F);
        operation.setReceiverId(1L);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(4L);
        user.setOperations(new HashSet());
        //When

        User result = userService.doPayement(operation, user.getId());
        //Then
        assertTrue(result.getBalance() == 0F);
    }

    @Test
    public void doPaymentWithNullInputTest() {
        //Then
        assertThrows(NullPointerException.class, () -> userService.doPayement(null, null));
    }

    @Test
    public void doPaymentWithUserNullInputTest() {
        //Given
        Operation operation = new Operation();
        operation.setAmount(100F);
        operation.setReceiverId(1L);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        //Then
        assertThrows(NullPointerException.class, () -> userService.doPayement(operation, null));
    }

    @Test
    public void doPaymentWithOperationNullInputTest() {
        //Given
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());
        //Then
        assertThrows(NullPointerException.class, () -> userService.doPayement(null, user.getId()));
    }

    @Test
    public void doPaymentWithOperationAmountNullInputTest() {
        //Given
        Operation operation = new Operation();
        operation.setReceiverId(1L);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());
        //Then
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user.getId()));
    }

    @Test
    public void doPaymentWithOperationAmountEqualZeroInputTest() {
        //Given
        Operation operation = new Operation();
        operation.setReceiverId(1L);
        operation.setAmount(0F);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());
        //Then
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user.getId()));
    }

    @Test
    public void doPayementWithUserReceiverNullTest() {
        //Given
        Operation operation = new Operation();
        operation.setAmount(100F);
        operation.setReceiverId(255L);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());
        //Then
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user.getId()));
    }

    @Test
    public void getUserByIdErrorTest() {
        //When
        //Then
        assertThrows(InvalidInputException.class, () -> userService.getUserById(28L));
    }

    @Test
    public void getUserByEmailTest() {
        //Given
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());
        //When
        Optional<User> result = userService.getUserByEmail("brice.morgat@gmx.fr");
        //Then
        assertEquals(result.get().getBalance(), 100F);
    }

    @Test
    public void getListContactTest() {
        //Given
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());

        Set<Contact> contacts = new HashSet();
        Contact contact = new Contact();
        contact.setContactId(1L);
        contacts.add(contact);

        //When
        //Then
        assertTrue(userService.getListContact(contacts).size() == 1);
    }

    @Test
    public void getListOperationTest() {
        //Given
        User user = new User();
        user.setEmail("brice.morgat@gmx.fr");
        user.setBalance(200F);
        user.setSurname("Surname");
        user.setId(2L);
        user.setOperations(new HashSet());

        Set<Operation> operations = new HashSet();
        Operation operation = new Operation();
        operation.setAmount(100F);
        operation.setReceiverId(1L);
        operation.setDescription("Description");
        operation.setDate(LocalDateTime.now());
        operations.add(operation);

        //When
        //Then
        assertTrue(userService.getListOperation(operations).size() == 1);
    }
}
