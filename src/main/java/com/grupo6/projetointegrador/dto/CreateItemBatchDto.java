package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.StorageType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateItemBatchDto {
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
