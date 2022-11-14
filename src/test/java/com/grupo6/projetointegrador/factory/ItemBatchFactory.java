package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.model.enumeration.StorageType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ItemBatchFactory {
    private static Long id;
    private static Product product;
    private static int productQuantity;
    private static LocalDate manufacturingDate;
    private static LocalDateTime manufacturingTime;
    private static Long volume;
    private static LocalDate dueDate;
    private static BigDecimal price;
    private static StorageType storageType;

    public static ItemBatch build(InboundOrder inboundOrder) {
        ItemBatch itemBatch = new ItemBatch();

        if (id == null) id = 1L;
        if (product == null) product = genericProduct();
        if (productQuantity == 0) productQuantity = 10;
        if (manufacturingDate == null) manufacturingDate = LocalDate.of(2022, 10, 20);
        if (manufacturingTime == null) manufacturingTime = LocalDateTime.of(2022, 10, 20, 1, 30, 10);
        if (volume == null) volume = 10L;
        if (dueDate == null) dueDate = LocalDate.of(2022, 11, 20);
        if (price == null) price = BigDecimal.valueOf(50);
        if (storageType == null) storageType =  StorageType.FRESCO;

        itemBatch.setId(id);
        itemBatch.setProduct(product);
        itemBatch.setPrice(price);
        itemBatch.setDueDate(dueDate);
        itemBatch.setVolume(volume);
        itemBatch.setPrice(price);
        itemBatch.setProductQuantity(productQuantity);
        itemBatch.setManufacturingDate(manufacturingDate);
        itemBatch.setManufacturingTime(manufacturingTime);
        itemBatch.setStorageType(storageType);

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
