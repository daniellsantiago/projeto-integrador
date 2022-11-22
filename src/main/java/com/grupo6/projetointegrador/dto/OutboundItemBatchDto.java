package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.OutboundItemBatch;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OutboundItemBatchDto {
    private Long outboundItemBatchId;

    private Long productId;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    public static OutboundItemBatchDto fromOutboundItemBatch(OutboundItemBatch outboundItemBatch) {
        return new OutboundItemBatchDto(
                outboundItemBatch.getId(),
                outboundItemBatch.getProduct().getId(),
                outboundItemBatch.getProductQuantity(),
                outboundItemBatch.getManufacturingDate(),
                outboundItemBatch.getManufacturingTime(),
                outboundItemBatch.getVolume(),
                outboundItemBatch.getDueDate(),
                outboundItemBatch.getPrice()
        );
    }
}
