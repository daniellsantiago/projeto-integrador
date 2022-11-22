package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.RefundPurchaseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.service.OrderPurchaseRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
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
