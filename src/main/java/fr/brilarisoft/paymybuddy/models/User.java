package fr.brilarisoft.paymybuddy.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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
    private String password;
}
