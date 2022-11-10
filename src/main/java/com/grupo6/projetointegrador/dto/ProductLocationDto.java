package com.grupo6.projetointegrador.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductLocationDto {
    private SectionDto sectionDto;
    private Long productId;
    private ItemBatchLocationDto itemBatchLocationDto;
}
