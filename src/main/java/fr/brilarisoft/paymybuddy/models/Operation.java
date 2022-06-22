package fr.brilarisoft.paymybuddy.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class Operation {
    @Id
    @SequenceGenerator(name = "operation_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_gen")
    private Long id;
    private Long receiverId;
    private Float amount;
    private String description;
    private LocalDateTime date;
}
