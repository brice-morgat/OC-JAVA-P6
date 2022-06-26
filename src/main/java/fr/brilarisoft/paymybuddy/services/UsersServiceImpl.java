package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.exceptions.InvalidInputException;
import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.models.dto.UserDTO;
import fr.brilarisoft.paymybuddy.repository.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsersServiceImpl implements IUserService {
    private final UsersRepo usersRepo;

    public UsersServiceImpl(UsersRepo usersRepo)  {
        this.usersRepo = usersRepo;
    }

    @Override
    public User saveUser(User user) {
        if(user.getEmail() != null && user.getBalance() != null) {
            return usersRepo.save(user);
        } else {
            throw new InvalidInputException("Invalid input for user");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return usersRepo.findUserByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        try {
            return usersRepo.findUserById(id);
        } catch (Exception e) {
            throw new InvalidInputException("Something went wrong");
        }
    }

    @Override
    public List<UserDTO> getListContact(Set<Contact> contacts) {
        List<UserDTO> result = new ArrayList();
        for (Contact contact : contacts) {
            User res = getUserById(contact.getContactId());
            UserDTO _contact = new UserDTO(res.getId(), res.getEmail());
            result.add(_contact);
        }
        return result;
    }

    @Override
    public List<OperationDTO> getListOperation(Set<Operation> operations) {
        List<OperationDTO> result = new ArrayList();
        for (Operation operation : operations) {
            User res = getUserById(operation.getReceiverId());
            OperationDTO operationDTO = new OperationDTO(res.getId(), res.getSurname(), operation.getDescription(), operation.getAmount(), operation.getDate());
            result.add(operationDTO);
        }
        Collections.sort(result);
        return result;
    }

    @Override
    @Transactional
    public User doPayement(Operation operation, User user) {
        if(operation == null || user == null) {
            throw new NullPointerException("At least one params is null");
        }
        if ( operation.getAmount() == null || operation.getAmount() <= 0) {
            throw new InvalidInputException("Amount of transaction invalid");
        }
        operation.setId(null);
        operation.setDate(LocalDateTime.now());
        System.out.println(user);
        User user1 = getUserById(user.getId());
        System.out.println(user1.toString());
        user1.setBalance(user1.getBalance() - operation.getAmount());
        user1.getOperations().add(operation);
        User user2 = getUserById(operation.getReceiverId());
        if (user2 == null) {
            throw new InvalidInputException("Something went wrong");
        }
        user2.setBalance(user2.getBalance() + operation.getAmount());
        return user1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = usersRepo.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), new ArrayList<>());
    }
}
