package fr.brilarisoft.paymybuddy.repository;

import fr.brilarisoft.paymybuddy.models.Contact;
import fr.brilarisoft.paymybuddy.models.Operation;
import fr.brilarisoft.paymybuddy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationsRepo extends JpaRepository<Operation, Long> {
    public Page<Operation> findAllByEmitterId(Long id, Pageable pageable);
}
