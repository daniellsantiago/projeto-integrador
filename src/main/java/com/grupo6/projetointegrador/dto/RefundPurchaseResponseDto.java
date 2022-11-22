package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.OrderPurchaseRefund;
import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundPurchaseResponseDto {
    private Long refundId;
    private RefundReason reason;

    public static RefundPurchaseResponseDto fromOrderPurchaseRefund(OrderPurchaseRefund orderPurchaseRefund) {
        return new RefundPurchaseResponseDto(
                orderPurchaseRefund.getId(),
                orderPurchaseRefund.getReason()
        );
    }
}
