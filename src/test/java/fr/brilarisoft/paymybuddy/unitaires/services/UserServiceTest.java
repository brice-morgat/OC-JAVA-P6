package fr.brilarisoft.paymybuddy.unitaires.services;

import fr.brilarisoft.paymybuddy.exceptions.InvalidInputException;
import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.ContactDTO;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.repository.UsersRepo;
import fr.brilarisoft.paymybuddy.services.IUserService;
import fr.brilarisoft.paymybuddy.services.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UsersServiceImpl userService;

    @Mock
    private UsersRepo usersRepo;

    @Test
    public void saveUserTest() {
        //Given
        User newUser = new User();
        newUser.setSurname("Ashley");
        newUser.setEmail("ashley@gmail.com");
        newUser.setPassword("Password");
        newUser.setBalance(100F);
        //When
        when(usersRepo.save(newUser)).thenReturn(newUser);
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
        user.setId(2L);
        user.setOperations(new HashSet());
        User user2 = new User();
        user2.setEmail("brice.morgat@gmx.fr");
        user2.setBalance(200F);
        user2.setSurname("Surname");
        user2.setId(1L);
        user2.setOperations(new HashSet());
        //When
        lenient().when(usersRepo.save(user)).thenReturn(user);
        when(usersRepo.findUserById(anyLong())).thenReturn(user);
        User result = userService.doPayement(operation, user);
        //Then
        assertTrue(result == user);
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
        assertThrows(NullPointerException.class, () -> userService.doPayement(null, user));
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
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user));
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
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user));
    }

    @Test
    public void doPayementWithUserReceiverNullTest() {
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
        user.setId(2L);
        user.setOperations(new HashSet());
        //When
        lenient().when(usersRepo.save(user)).thenReturn(user);
        when(usersRepo.findUserById(anyLong())).thenReturn(user).thenReturn(null);
        //Then
        assertThrows(InvalidInputException.class, () -> userService.doPayement(operation, user));
    }

    @Test
    public void getUserByIdErrorTest() {
        //When
        when(usersRepo.findUserById(anyLong())).thenThrow(InvalidInputException.class);
        //Then
        assertThrows(InvalidInputException.class, () -> userService.getUserById(1L));
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
        when(usersRepo.findUserByEmail("")).thenReturn(java.util.Optional.of(user));
        //Then
        assertEquals(userService.getUserByEmail(""), java.util.Optional.of(user));
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
        when(usersRepo.findUserById(anyLong())).thenReturn(user);
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
        when(usersRepo.findUserById(anyLong())).thenReturn(user);
        //Then
        assertTrue(userService.getListOperation(operations).size() == 1);
    }
}

