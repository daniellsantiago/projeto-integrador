package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.RefundPurchaseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.ItemBatchFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRefundRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderPurchaseRefundServiceImplTest {

    @Mock
    private OrderPurchaseRepo orderPurchaseRepo;

    @Mock
    private OrderPurchaseRefundRepo orderPurchaseRefundRepo;

    @Mock
    private ItemBatchRepo itemBatchRepo;

    @InjectMocks
    private OrderPurchaseRefundServiceImpl refundService;

    @Test
    void refund_createRefundAndUpdateOrderStatusAndUpdateItemStock_whenReasonIsArrependimentoAndDateOrderIsValid() {
        // Given
        List<Product> products = createProducts();
        OrderPurchase orderPurchase = createOrderPurchase(products, LocalDate.now());
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                orderPurchase.getBuyer().getId(),
                RefundReason.ARREPENDIMENTO
        );
        ItemBatch itemBatch1 = createItemBatch(products.get(0));
        ItemBatch itemBatch2 = createItemBatch(products.get(1));
        int itemBatch1QntAfterRefund = orderPurchase.getProductOrders().stream()
                .filter(productOrder -> productOrder.getProduct().getId().equals(itemBatch1.getProduct().getId()))
                .findFirst().get().getQuantity() + itemBatch1.getProductQuantity();
        int itemBatch2QntAfterRefund = orderPurchase.getProductOrders().stream()
                .filter(productOrder -> productOrder.getProduct().getId().equals(itemBatch2.getProduct().getId()))
                .findFirst().get().getQuantity() + itemBatch2.getProductQuantity();
        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        // When
        when(orderPurchaseRepo.findById(refundPurchaseDto.getPurchaseId())).thenReturn(Optional.of(orderPurchase));
        when(itemBatchRepo.findByProductIdIn(productIds)).thenReturn(List.of(itemBatch1, itemBatch2));
        when(orderPurchaseRefundRepo.save(ArgumentMatchers.any())).thenReturn(
                new OrderPurchaseRefund(1L, orderPurchase, refundPurchaseDto.getReason(), LocalDate.now())
        );
        RefundPurchaseResponseDto response = refundService.refund(refundPurchaseDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getRefundId()).isEqualTo(1L);
        assertThat(response.getReason()).isEqualTo(refundPurchaseDto.getReason());

        assertThat(itemBatch1.getProductQuantity()).isEqualTo(itemBatch1QntAfterRefund);
        assertThat(itemBatch2.getProductQuantity()).isEqualTo(itemBatch2QntAfterRefund);

        assertThat(orderPurchase.getStatus()).isEqualTo(StatusOrder.REEMBOLSADO);
        verify(orderPurchaseRepo, times(1)).save(ArgumentMatchers.any());
        verify(orderPurchaseRefundRepo, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void refund_NotFoundException_whenOrderPurchaseDoesNotExists() {
        // Given
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                1L,
                1L,
                RefundReason.DEFEITO
        );
        // When / Then
        assertThatThrownBy(() -> refundService.refund(refundPurchaseDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void refund_throwBusinessException_whenReasonIsArrependimentoAndDateOrderIsGreaterThan7Days() {
        // Given
        List<Product> products = createProducts();
        OrderPurchase orderPurchase = createOrderPurchase(products, LocalDate.now().minusDays(10));
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                orderPurchase.getBuyer().getId(),
                RefundReason.ARREPENDIMENTO
        );
        // When / Then
        when(orderPurchaseRepo.findById(refundPurchaseDto.getPurchaseId())).thenReturn(Optional.of(orderPurchase));
        assertThatThrownBy(() -> refundService.refund(refundPurchaseDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void refund_throwBusinesssException_whenReasonIsDefeitoAndDateOrderIsGreaterThan90Days() {
        // Given
        List<Product> products = createProducts();
        OrderPurchase orderPurchase = createOrderPurchase(products, LocalDate.now().minusDays(91));
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                orderPurchase.getBuyer().getId(),
                RefundReason.DEFEITO
        );
        // When / Then
        when(orderPurchaseRepo.findById(refundPurchaseDto.getPurchaseId())).thenReturn(Optional.of(orderPurchase));
        assertThatThrownBy(() -> refundService.refund(refundPurchaseDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    private List<Product> createProducts() {
        Seller seller = new Seller(1L, List.of());
        Product product1 = new Product(1L, BigDecimal.valueOf(4), Category.REFRIGERADO, seller);
        Product product2 = new Product(2L, BigDecimal.valueOf(10), Category.REFRIGERADO, seller);
        seller.setProducts(List.of(product1, product2));

        return List.of(product1, product2);
    }

    private ItemBatch createItemBatch(Product product) {
        Section section = WarehouseFactory.build().getSections().get(0);
        section.setCategory(product.getCategory());

        InboundOrder inboundOrder = InboundOrderFactory.build(section);
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        itemBatch.setProduct(product);
        itemBatch.setProductQuantity(50);
        itemBatch.setCategory(product.getCategory());

        return itemBatch;
    }

    private OrderPurchase createOrderPurchase(List<Product> products, LocalDate dateOrder) {
        Buyer buyer = new Buyer(1L, List.of());
        OrderPurchase orderPurchase = new OrderPurchase(1L, buyer, dateOrder, List.of(), StatusOrder.FINALIZADO);
        ProductOrder productOrder1 = new ProductOrder(1L, orderPurchase, products.get(0), 1);
        ProductOrder productOrder2 = new ProductOrder(2L, orderPurchase, products.get(1), 5);
        orderPurchase.setProductOrders(List.of(productOrder1, productOrder2));
        buyer.setOrders(List.of(orderPurchase));

        return orderPurchase;
    }
}
