package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInboundOrderDto {
    private Long warehouseOperatorId;
    private Long warehouseId;
    private Long sectionId;
    private List<CreateItemBatchDto> itemBatches;
}
