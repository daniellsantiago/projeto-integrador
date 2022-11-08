package com.grupo6.projetointegrador.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateInboundOrderDto {
    private Long warehouseId;
    private Long sectionId;
    private List<CreateItemBatchDto> createItemBatchDtos;
}
