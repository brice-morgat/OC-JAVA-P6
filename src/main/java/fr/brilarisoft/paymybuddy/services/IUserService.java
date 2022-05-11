package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    /**
     * Add a User
     * @param user
     * @return Saved User
     */
    public User saveUser(User user);
}
