package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.OutboundItemBatch;
import com.grupo6.projetointegrador.model.entity.OutboundOrder;
import com.grupo6.projetointegrador.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OutboundItemBatchDto {
    private Long itemBatchId;

    private Long productId;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    public static OutboundItemBatchDto fromItemBatch(ItemBatch itemBatch) {
        return new OutboundItemBatchDto(
                itemBatch.getId(),
                itemBatch.getProduct().getId(),
                itemBatch.getProductQuantity(),
                itemBatch.getManufacturingDate(),
                itemBatch.getManufacturingTime(),
                itemBatch.getVolume(),
                itemBatch.getDueDate(),
                itemBatch.getPrice()
        );
    }

    public static OutboundItemBatchDto fromOutboundItemBatch(OutboundItemBatch itemBatch) {
        return new OutboundItemBatchDto(
                itemBatch.getId(),
                itemBatch.getProduct().getId(),
                itemBatch.getProductQuantity(),
                itemBatch.getManufacturingDate(),
                itemBatch.getManufacturingTime(),
                itemBatch.getVolume(),
                itemBatch.getDueDate(),
                itemBatch.getPrice()
        );
    }
}
