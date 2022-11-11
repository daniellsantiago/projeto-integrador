package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductLocationDto {
    private SectionDto sectionDto;
    private Long productId;
    private List<ItemBatchLocationDto> itemBatchLocationDto;
}
