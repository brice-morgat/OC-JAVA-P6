package fr.brilarisoft.paymybuddy.models.dto;

import fr.brilarisoft.paymybuddy.models.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OperationDTO implements Comparable<OperationDTO> {
    private Long id;
    private String username;
    private String description;
    private Float amount;
    private LocalDateTime date;

    @Override
    public int compareTo(OperationDTO o) {
        return o.getDate().compareTo(getDate());
    }
}
