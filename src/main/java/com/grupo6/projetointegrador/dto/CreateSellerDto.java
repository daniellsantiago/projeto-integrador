package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Integer houseNumber;
    private String zipCode;
}
