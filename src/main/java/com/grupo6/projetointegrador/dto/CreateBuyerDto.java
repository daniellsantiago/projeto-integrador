package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuyerDto {
    private String name;
    private String cpf;
    private String address;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}
