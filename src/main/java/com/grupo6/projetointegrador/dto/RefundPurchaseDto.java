package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundPurchaseDto {
    @NotNull
    @Positive
    private Long purchaseId;

    @NotNull
    @Positive
    private Long buyerId;

    @NotNull
    private RefundReason reason;
}
