package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.ProductOrder;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
