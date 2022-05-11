package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UsersServiceImpl implements IUserService {
    private final UsersRepo usersRepo;

    @Autowired
    public UsersServiceImpl(UsersRepo usersRepo)  {
        this.usersRepo = usersRepo;
    }


    @Override
    public User saveUser(User user) {
        return usersRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = usersRepo.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), new ArrayList<>());
    }
}
