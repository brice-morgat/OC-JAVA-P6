package fr.brilarisoft.paymybuddy.repository;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UsersRepo extends JpaRepository<User, Long> {
    public User deleteUserById(Long id);

    public User findUserById(Long id);

    public Optional<User> findUserByEmail(String email);
}
