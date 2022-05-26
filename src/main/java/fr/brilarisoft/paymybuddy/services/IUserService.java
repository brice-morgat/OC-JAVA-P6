package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    /**
     * Add a User
     * @param user
     * @return Saved User
     */
    public User saveUser(User user);

    public Optional<User> getUserByEmail(String email);

    public List<User> getAll();

    public User getUserById(Long id);
}
