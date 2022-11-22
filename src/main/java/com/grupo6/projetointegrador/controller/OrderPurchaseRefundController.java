package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.RefundPurchaseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.service.OrderPurchaseRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/order-purchase-refund")
@RequiredArgsConstructor
public class OrderPurchaseRefundController {
    private final OrderPurchaseRefundService orderPurchaseRefundService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RefundPurchaseResponseDto refund(@RequestBody @Valid RefundPurchaseDto refundPurchaseDto) {
        return orderPurchaseRefundService.refund(refundPurchaseDto);
    }
}
