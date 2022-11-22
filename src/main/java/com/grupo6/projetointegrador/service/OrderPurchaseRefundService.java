package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseDto;

public interface OrderPurchaseRefundService {
    RefundPurchaseResponseDto refund(RefundPurchaseDto refundPurchaseDto);
}
