package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOrderDto {

    private Long productId;

    private int quantity;

    public static ProductOrderDto fromProductOrder(ProductOrder productOrder) {
        return new ProductOrderDto(
                productOrder.getProductId(),
                productOrder.getQuantity()
        );
    }

    public static ProductOrder toProductOrder(ProductOrderDto productOrderDto, OrderPurchase orderPurchase) {
        return new ProductOrder(
                orderPurchase,
                productOrderDto.getProductId(),
                productOrderDto.getQuantity()
        );
    }
}
