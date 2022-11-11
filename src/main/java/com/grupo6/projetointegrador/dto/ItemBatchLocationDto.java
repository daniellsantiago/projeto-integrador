package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ItemBatchLocationDto {
    private Long itemBatchId;
    private int productQuantity;
    private LocalDate dueDate;
    private Long sectionId;
    private Long warehouseId;

    public static ItemBatchLocationDto fromItemBatch(ItemBatch itemBatch){
        return new ItemBatchLocationDto(
                itemBatch.getId(),
                itemBatch.getProductQuantity(),
                itemBatch.getDueDate(),
                itemBatch.getInboundOrder().getSection().getId(),
                itemBatch.getInboundOrder().getWarehouse().getId());
    }
}
