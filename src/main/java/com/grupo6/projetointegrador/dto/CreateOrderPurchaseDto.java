package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderPurchaseDto {
    @NotNull
    @Positive
    private Long buyer;

    @NotNull
    private LocalDate dateOrder;

    @NotEmpty
    @Valid
    private List<ProductOrderDto> productOrders;
}
