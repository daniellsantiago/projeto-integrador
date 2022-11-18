package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemBatchDto {
    private Long itemBatchId;

    private Long productId;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    public static ItemBatchDto fromItemBatch(ItemBatch itemBatch) {
        return new ItemBatchDto(
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
