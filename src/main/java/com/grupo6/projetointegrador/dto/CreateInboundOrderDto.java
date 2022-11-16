package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInboundOrderDto {
    @NotNull
    private Long warehouseOperatorId;

    @NotNull
    private Long warehouseId;

    @NotNull
    private Long sectionId;

    @NotEmpty
    @Valid
    private List<CreateItemBatchDto> itemBatches;
}
