package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateItemBatchDto {
    @NotNull
    private Long productId;

    @Positive
    private int productQuantity;

    @NotNull
    private LocalDate manufacturingDate;

    @NotNull
    private LocalDateTime manufacturingTime;

    @NotNull
    @Positive
    private Long volume;

    @NotNull
    private LocalDate dueDate;

    @Positive
    private BigDecimal price;

    public ItemBatch toItemBatch(InboundOrder inboundOrder, Product product) {
        return new ItemBatch(
                product,
                productQuantity,
                manufacturingDate,
                manufacturingTime,
                volume,
                dueDate,
                price,
                inboundOrder
        );
    }
}
