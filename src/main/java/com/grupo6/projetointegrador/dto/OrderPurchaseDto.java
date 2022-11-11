package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderPurchaseDto {
    private Long id;

    private LocalDate dateOrder;

    private StatusOrder status;

    private List<ProductOrderDto> productOrders;

    public static OrderPurchaseDto fromOrderPurchase(OrderPurchase orderPurchase, List<ProductOrderDto> productOrderDto) {
        return new OrderPurchaseDto(
                orderPurchase.getId(),
                orderPurchase.getDateOrder(),
                orderPurchase.getStatus(),
                productOrderDto
        );
    }
}
