package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.Data;

@Data
public class ListRefundsParamsDto {
    private final Long buyerId;
    private final Long sellerId;
    private final RefundReason reason;
}
