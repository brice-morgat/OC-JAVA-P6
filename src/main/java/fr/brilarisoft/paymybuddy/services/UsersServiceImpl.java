package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.repository.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UsersServiceImpl implements IUserService {
    private final UsersRepo usersRepo;

    public UsersServiceImpl(UsersRepo usersRepo)  {
        this.usersRepo = usersRepo;
    }

    @Override
    public User saveUser(User user) {
        return usersRepo.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return usersRepo.findUserByEmail(email);
    }

    public List<User> getAll() {
        return usersRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return usersRepo.findUserById(id);
    }

    @Transactional
    public User doPayement(User user, Long idReceiver,Float amount, String description) {
        Operation operation = new Operation();
        operation.setReceiverId(idReceiver);
        operation.setAmount(amount);
        operation.setDescription(description);
        user.getOperations().add(operation);
        user.setBalance(user.getBalance() - amount);
        saveUser(user);
        User user2 = getUserById(idReceiver);
        user2.setBalance(user2.getBalance() + amount);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = usersRepo.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), new ArrayList<>());
    }
}
