package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ListRefundDto {
    private Long id;

    private Long purchaseId;

    private RefundReason reason;

    private LocalDate refundDate;
}
