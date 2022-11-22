package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.service.OrderPurchaseRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    public List<ListRefundDto> listRefunds(ListRefundsParamsDto listRefundsParamsDto) {
        return orderPurchaseRefundService.listRefundsFiltered(listRefundsParamsDto);
}

    @GetMapping("/{id}")
    public OrderPurchaseRefundDto getRefundById(@PathVariable Long id) {
        return orderPurchaseRefundService.getRefundById(id);
    }
}
