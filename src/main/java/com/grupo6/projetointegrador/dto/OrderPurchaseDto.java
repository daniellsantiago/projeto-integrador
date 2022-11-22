package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderPurchaseDto {
    private Long id;

    private LocalDate dateOrder;

    private StatusOrder status;

    private List<ProductOrderDto> productOrders;

    public static OrderPurchaseDto fromOrderPurchase(OrderPurchase orderPurchase) {
        List<ProductOrderDto> productOrderDtos = orderPurchase.getProductOrders().stream().
                map(ProductOrderDto::fromProductOrder).collect(Collectors.toList());
        return new OrderPurchaseDto(
                orderPurchase.getId(),
                orderPurchase.getDateOrder(),
                orderPurchase.getStatus(),
                productOrderDtos
        );
    }
}
