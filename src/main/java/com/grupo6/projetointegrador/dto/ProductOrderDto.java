package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOrderDto {
    private Long id;

    private Long productId;

    private Long quantity;

    public static ProductOrderDto fromProductOrder(ProductOrder productOrder) {
        return new ProductOrderDto(
                productOrder.getId(),
                productOrder.getProductId(),
                productOrder.getQuantity()
        );
    }
}
