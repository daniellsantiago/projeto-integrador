package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOutboundItemBatchDto {
    private Long outItemBatchId;

    @NotNull
    private Long productId;

    @Positive
    private int productQuantity;

    @NotNull
    private LocalDate manufacturingDate;

    @NotNull
    private LocalDateTime manufacturingTime;

    @Positive
    @NotNull
    private Long volume;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    @Positive
    private BigDecimal price;

    public OutboundItemBatch toOutboundItemBatch(OutboundOrder outboundOrder, Product product) {
        return new OutboundItemBatch(
                outItemBatchId,
                product,
                productQuantity,
                manufacturingDate,
                manufacturingTime,
                volume,
                dueDate,
                price,
                outboundOrder,
                product.getCategory()
        );
    }
}
