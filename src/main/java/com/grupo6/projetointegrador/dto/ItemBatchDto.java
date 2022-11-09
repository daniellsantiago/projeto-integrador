package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.InboundOrder;
import com.grupo6.projetointegrador.model.ItemBatch;
import com.grupo6.projetointegrador.model.Product;
import com.grupo6.projetointegrador.model.StorageType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemBatchDto {
    private Long itemBatchId;

    private Long productId;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    private StorageType storageType;

    public ItemBatch toItemBatch(InboundOrder inboundOrder, Product product) {
        return new ItemBatch(
                itemBatchId,
                product,
                productQuantity,
                manufacturingDate,
                manufacturingTime,
                volume,
                dueDate,
                price,
                inboundOrder,
                storageType
        );
    }
}
