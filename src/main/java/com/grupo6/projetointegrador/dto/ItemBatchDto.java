package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.StorageType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemBatchDto {
    private Long id;

    private Long productId;

    private int productQuantity;

    private LocalDate manufacturingDate;

    private LocalDateTime manufacturingTime;

    private Long volume;

    private LocalDate dueDate;

    private BigDecimal price;

    private StorageType storageType;
}
