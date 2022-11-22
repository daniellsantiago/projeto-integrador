package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ItemBatchFactory {
    public static ItemBatch build(InboundOrder inboundOrder) {
        ItemBatch itemBatch = new ItemBatch();

        itemBatch.setId(1L);
        itemBatch.setProduct(genericProduct());
        itemBatch.setPrice(BigDecimal.valueOf(50));
        itemBatch.setDueDate(LocalDate.of(2022, 11, 20));
        itemBatch.setVolume(10L);
        itemBatch.setProductQuantity(10);
        itemBatch.setManufacturingDate(LocalDate.of(2022, 10, 20));
        itemBatch.setManufacturingTime(LocalDateTime.of(2022, 10, 20, 1, 30, 10));

        itemBatch.setInboundOrder(inboundOrder);

        return itemBatch;
    }

    private static Product genericProduct() {
        Seller seller = new Seller(1L, null);

        Product product = new Product(1L, BigDecimal.valueOf(5), Category.FRESCO, seller);

        seller.setProducts(List.of(product));

        return product;
    }
}
