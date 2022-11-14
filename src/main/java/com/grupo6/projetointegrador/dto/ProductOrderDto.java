package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class ProductOrderDto {
    @NotNull
    private Long productId;

    @Positive
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
