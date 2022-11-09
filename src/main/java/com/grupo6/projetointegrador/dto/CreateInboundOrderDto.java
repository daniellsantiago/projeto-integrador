package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.InboundOrder;
import lombok.Data;

import java.util.List;

@Data
public class CreateInboundOrderDto {
    private Long warehouseOperatorId;
    private Long warehouseId;
    private Long sectionId;
    private List<CreateItemBatchDto> createItemBatchDtos;
}
