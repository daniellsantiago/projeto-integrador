package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRefundRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderPurchaseRefundServiceImpl implements OrderPurchaseRefundService {
    private final OrderPurchaseRepo orderPurchaseRepo;

    private final ItemBatchRepo itemBatchRepo;

    private final OrderPurchaseRefundRepo orderPurchaseRefundRepo;

    @Override
    @Transactional
    public RefundPurchaseResponseDto refund(RefundPurchaseDto refundPurchaseDto) {
        OrderPurchase orderPurchase = orderPurchaseRepo.findByIdAndStatus(refundPurchaseDto.getPurchaseId(), StatusOrder.FINALIZADO)
                .orElseThrow(() -> new NotFoundException("Compra não encontrada"));

        if (!isOrderPurchaseRefundable(orderPurchase, refundPurchaseDto.getReason())) {
            throw new BusinessRuleException("A compra não está no prazo válido para reembolso.");
        }

        if (refundPurchaseDto.getReason().equals(RefundReason.ARREPENDIMENTO)) {
            returnProductsToWarehouse(orderPurchase);
        }

        refundOrderPurchase(orderPurchase);

        OrderPurchaseRefund createdOrderPurchaseRefund = orderPurchaseRefundRepo.save(
                new OrderPurchaseRefund(null, orderPurchase, refundPurchaseDto.getReason(), LocalDate.now())
        );
        return RefundPurchaseResponseDto.fromOrderPurchaseRefund(createdOrderPurchaseRefund);
    }

    private boolean isOrderPurchaseRefundable(OrderPurchase orderPurchase, RefundReason refundReason) {
        if (refundReason.equals(RefundReason.ARREPENDIMENTO)) {
            return orderPurchase.getDateOrder().plusDays(7).isAfter(LocalDate.now()) ||
                    orderPurchase.getDateOrder().plusDays(7).isEqual(LocalDate.now());
        }

        return orderPurchase.getDateOrder().plusDays(90).isAfter(LocalDate.now()) ||
                orderPurchase.getDateOrder().plusDays(90).isEqual(LocalDate.now());
    }

    private void returnProductsToWarehouse(OrderPurchase orderPurchase) {
        List<ProductOrder> productOrders = orderPurchase.getProductOrders();
        List<ItemBatch> itemBatches = itemBatchRepo.findByProductIdIn(
                productOrders.stream()
                        .map(ProductOrder::getProduct)
                        .map(Product::getId)
                        .collect(Collectors.toList())
        );

        itemBatches.forEach(itemBatch -> {
            ProductOrder itemBatchProductOrder = productOrders.stream()
                    .filter(productOrder -> productOrder.getProduct().getId().equals(itemBatch.getProduct().getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Lote não encontrado."));
            itemBatch.setProductQuantity(itemBatch.getProductQuantity() + itemBatchProductOrder.getQuantity());
        });

        itemBatchRepo.saveAll(itemBatches);
    }

    private void refundOrderPurchase(OrderPurchase orderPurchase) {
        orderPurchase.setStatus(StatusOrder.REEMBOLSADO);
        orderPurchaseRepo.save(orderPurchase);
    }
}
