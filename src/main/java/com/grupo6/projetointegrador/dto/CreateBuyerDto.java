package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuyerDto {
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Endereço é obrigatório")
    private String address;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatório")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^[0-9]*$", message = "CEP deve conter apenas números")
    @Size(min = 8, max = 8)
    private String zipCode;
}
