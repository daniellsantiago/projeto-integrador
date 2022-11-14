package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.Product;
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
                productOrder.getProduct().getId(),
                productOrder.getQuantity()
        );
    }

    public ProductOrder toProductOrder(OrderPurchase orderPurchase, Product product) {
        return new ProductOrder(
                orderPurchase,
                product,
                quantity
        );
    }
}
