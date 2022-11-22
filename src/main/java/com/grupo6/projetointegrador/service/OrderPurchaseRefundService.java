package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ListRefundsParamsDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseRefundDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseDto;

import java.util.List;

public interface OrderPurchaseRefundService {
    RefundPurchaseResponseDto refund(RefundPurchaseDto refundPurchaseDto);
    List<OrderPurchaseRefundDto> listRefundsFiltered(ListRefundsParamsDto listRefundsParamsDto);
}
