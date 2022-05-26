package fr.brilarisoft.paymybuddy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class User {
    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;
    private String email;
    private String surname;
    private String password;
    private Float balance;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Contact> contacts;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "emitter_id")
    private Set<Operation> operations;
}
