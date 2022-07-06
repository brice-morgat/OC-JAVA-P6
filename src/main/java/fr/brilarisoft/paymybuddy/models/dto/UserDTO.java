package fr.brilarisoft.paymybuddy.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String email;
}
