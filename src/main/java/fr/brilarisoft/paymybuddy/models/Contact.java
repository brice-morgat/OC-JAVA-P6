package fr.brilarisoft.paymybuddy.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Contact {
    @Id
    @SequenceGenerator(name = "contact_gen", sequenceName = "contact_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_gen")
    private Long id;
    private Long contactId;
}