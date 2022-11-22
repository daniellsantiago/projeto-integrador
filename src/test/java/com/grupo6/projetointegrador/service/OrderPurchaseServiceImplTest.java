package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.ProductOrderDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class OrderPurchaseServiceImplTest {
    @Mock
    private OrderPurchaseRepo orderPurchaseRepo;

    @Mock
    private BuyerRepo buyerRepo;

    @Mock
    private ItemBatchRepo batchRepo;

    @InjectMocks
    private OrderPurchaseServiceImpl orderPurchaseService;

    @Test
    void endOrder_updateOrderStatusAndStock_whenOrderExistsAndHasAbertoStatus() {
        // Given
        Long orderId = 1L;
        ItemBatch itemBatch = setupGenericItemBatch();
        Product product = itemBatch.getProduct();

        OrderPurchase orderPurchase = setupGenericOrderPurchase(product);
        ProductOrder productOrder = orderPurchase.getProductOrders().get(0);
        int initialProductQuantity = itemBatch.getProductQuantity();

        // When
        Mockito.when(orderPurchaseRepo.findById(orderId)).thenReturn(Optional.of(orderPurchase));
        Mockito.when(batchRepo.findByDueDate21AndProductIdAndQty(product.getId(), productOrder.getQuantity()))
                .thenReturn(Optional.of(itemBatch));
        String result = orderPurchaseService.endOrder(orderId);

        // Then
        assertThat(result).isEqualTo("Pedido finalizado com sucesso!");
        assertThat(orderPurchase.getStatus()).isEqualTo(StatusOrder.FINALIZADO);
        assertThat(initialProductQuantity).isGreaterThan(itemBatch.getProductQuantity());
    }

    @Test
    void endOrder_throwNotFoundException_whenOrderDoesNotExists() {
        // Given
        Long orderId = 1L;

        // When / Then
        Mockito.when(orderPurchaseRepo.findById(orderId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderPurchaseService.endOrder(orderId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void endOrder_throwBusinessRuleException_whenOrderStatusIsFinalizado() {
        // Given
        Long orderId = 1L;
        ItemBatch itemBatch = setupGenericItemBatch();
        Product product = itemBatch.getProduct();

        OrderPurchase orderPurchase = setupGenericOrderPurchase(product);
        orderPurchase.setStatus(StatusOrder.FINALIZADO);
        // When / Then
        Mockito.when(orderPurchaseRepo.findById(orderId)).thenReturn(Optional.of(orderPurchase));
        assertThatThrownBy(() -> orderPurchaseService.endOrder(orderId))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createOrderPurchase_createsOrderPurchase_whenDtoValidAndBuyerExists() {
        // Given
        ItemBatch itemBatch = setupGenericItemBatch();

        Buyer buyer = new Buyer(1L, null, null, null, null, null, null, null, null, null);;
        CreateOrderPurchaseDto createOrderPurchaseDto = new CreateOrderPurchaseDto(
                1L,
                LocalDate.now(),
                List.of(new ProductOrderDto(1L, 2))
        );

        // When
        Mockito.when(buyerRepo.findById(1L)).thenReturn(Optional.of(buyer));
        Mockito.when(batchRepo.findByDueDate21AndProductIdAndQty(1L, 2))
                .thenReturn(Optional.of(itemBatch));
        TotalPriceDto totalPriceDto = orderPurchaseService.createOrderPurchase(createOrderPurchaseDto);
        // Then
        assertThat(totalPriceDto).isNotNull();
        assertThat(totalPriceDto.getTotalPrice()).isEqualTo(10);
    }

    @Test
    void createOrderPurchase_throwNotFoundException_whenBuyerDoesNotExists() {
        // Given
        CreateOrderPurchaseDto createOrderPurchaseDto = new CreateOrderPurchaseDto(
                1L,
                LocalDate.now(),
                List.of(new ProductOrderDto(1L, 2))
        );

        // When / Then
        Mockito.when(buyerRepo.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderPurchaseService.createOrderPurchase(createOrderPurchaseDto))
                .isInstanceOf(NotFoundException.class);
    }

    private ItemBatch setupGenericItemBatch() {
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));

        return inboundOrder.getItemBatches().get(0);
    }

    private OrderPurchase setupGenericOrderPurchase(Product product) {
        Buyer buyer = new Buyer(1L, null, null, null, null, null, null, null, null, null);
        OrderPurchase orderPurchase = new OrderPurchase(1L, buyer, LocalDate.now(), null, StatusOrder.ABERTO);
        ProductOrder productOrder = new ProductOrder(1L, orderPurchase, product, 2);

        orderPurchase.setProductOrders(List.of(productOrder));
        buyer.setOrders(List.of(orderPurchase));

        return orderPurchase;
    }
}
