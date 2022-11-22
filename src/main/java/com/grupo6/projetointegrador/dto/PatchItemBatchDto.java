package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PatchItemBatchDto {
    private Integer productQuantity;
    private Long volume;
    private BigDecimal price;

}
