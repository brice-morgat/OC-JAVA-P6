package fr.brilarisoft.paymybuddy.services;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import fr.brilarisoft.paymybuddy.models.dto.OperationDTO;
import fr.brilarisoft.paymybuddy.models.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserService extends UserDetailsService {
    /**
     * Add a User
     * @param user
     * @return Saved User
     */
    public User saveUser(User user);

    /**
     * Get a user by email, or return null if no user is found.
     *
     * @param email The email of the user to be retrieved.
     * @return Optional<User>
     */
    public Optional<User> getUserByEmail(String email);

    /**
     * This function returns a User object with the given id.
     *
     * @param id The id of the user to be retrieved.
     * @return A User object
     */
    public User getUserById(Long id);

    /**
     * It returns a list of UserDTO objects.
     *
     * @param contacts A set of contacts
     * @return A list of UserDTO objects.
     */
    public List<UserDTO> getListContact(Set<Contact> contacts);

    /**
     * It returns a list of OperationDTO objects.
     *
     * @param operations a set of operations
     * @return A list of OperationDTO objects.
     */
    public List<OperationDTO> getListOperation(Set<Operation> operations);

    /**
     * A method that is used to do a payment.
     *
     * @param operation and user
     * @return The user emitter of the transaction
     */
    @Transactional
    public User doPayement(Operation operation, User user);
}
