package fr.brilarisoft.paymybuddy.config;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DatabaseInitService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private final IUserService userService;

    public DatabaseInitService(IUserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void initBDD() {
        addPerson("haley@gmail.com", "Haley", "Haley");
        addPerson("clara@gmail.com", "Clara", "Clara");
        addPerson("smith@gmail.com", "Smith", "Smith");
        addPerson("brice.morgat@gmx.fr",  "Brice", "brice");
    }

    private User addPerson(String email, String surname, String password) {
        Contact contact1 = new Contact();
        contact1.setContactId(1L);
        Set<Contact> contacts = new HashSet();
        contacts.add(contact1);

        Optional<User> isExist = userService.getUserByEmail(email);
        if (isExist.isPresent()) {
            User existing = isExist.get();
            existing.setEmail(email);
            existing.setPassword(passwordEncoder.encode(password));
            existing.setSurname(surname);
            existing.setContacts(contacts);
            existing.setBalance(100F);
            userService.saveUser(existing);
            return existing;
        }
        User userToAdd = new User();
        userToAdd.setEmail(email);
        userToAdd.setPassword(passwordEncoder.encode(password));
        userToAdd.setSurname(surname);
        userToAdd.setContacts(contacts);
        userService.saveUser(userToAdd);
        return userToAdd;
    }
}
