package fr.brilarisoft.paymybuddy.repository;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactsRepo extends JpaRepository<Contact, Long> {
    public List<Contact> findAllByEmitterId(Long id, Pageable pageable);
}
