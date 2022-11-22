package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;

import java.util.List;

public interface OrderPurchaseRefundService {
    RefundPurchaseResponseDto refund(RefundPurchaseDto refundPurchaseDto);
    List<ListRefundDto> listRefundsFiltered(ListRefundsParamsDto listRefundsParamsDto);
    OrderPurchaseRefundDto getRefundById(Long id);
}
