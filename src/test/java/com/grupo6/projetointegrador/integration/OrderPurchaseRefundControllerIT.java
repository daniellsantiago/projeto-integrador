package com.grupo6.projetointegrador.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo6.projetointegrador.dto.ListRefundDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseRefundDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseDto;
import com.grupo6.projetointegrador.dto.RefundPurchaseResponseDto;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderPurchaseRefundControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderPurchaseRepo orderPurchaseRepo;

    @Autowired
    private OrderPurchaseRefundRepo orderPurchaseRefundRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ItemBatchRepo itemBatchRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Test
    void refund_saveRefundAndUpdateOrderStatusAndUpdateItemStock_whenReasonIsArrependimentoAndDateOrderIsValid() throws Exception {
        // Given
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.FINALIZADO, LocalDate.now());
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                RefundReason.ARREPENDIMENTO
        );
        int itemBatch1QntBeforeRefund = itemBatches.get(0).getProductQuantity();
        int itemBatch2QntBeforeRefund = itemBatches.get(1).getProductQuantity();

        // When
        MvcResult result = mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isCreated())
                .andReturn();
        RefundPurchaseResponseDto refundPurchaseResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                RefundPurchaseResponseDto.class
        );

        OrderPurchase updatedPurchase = orderPurchaseRepo.findById(orderPurchase.getId()).get();
        ItemBatch updatedItem1 = itemBatchRepo.findById(itemBatches.get(0).getId()).get();
        ItemBatch updatedItem2 = itemBatchRepo.findById(itemBatches.get(1).getId()).get();

        // Then
        assertThat(refundPurchaseResponseDto).isNotNull();
        assertThat(refundPurchaseResponseDto.getRefundId()).isEqualTo(1L);
        assertThat(refundPurchaseResponseDto.getReason()).isEqualTo(RefundReason.ARREPENDIMENTO);

        assertThat(updatedPurchase.getStatus()).isEqualTo(StatusOrder.REEMBOLSADO);

        assertThat(updatedItem1.getProductQuantity()).isGreaterThan(itemBatch1QntBeforeRefund);
        assertThat(updatedItem2.getProductQuantity()).isGreaterThan(itemBatch2QntBeforeRefund);
    }

    @Test
    void refund_saveRefundAndUpdateOrderStatus_whenReasonIsDefeitoAndDateOrderIsValid() throws Exception {
        // Given
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.FINALIZADO, LocalDate.now());
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                RefundReason.DEFEITO
        );

        // When
        MvcResult result = mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isCreated())
                .andReturn();
        RefundPurchaseResponseDto refundPurchaseResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                RefundPurchaseResponseDto.class
        );
        OrderPurchase updatedPurchase = orderPurchaseRepo.findById(orderPurchase.getId()).get();

        // Then
        assertThat(refundPurchaseResponseDto).isNotNull();
        assertThat(refundPurchaseResponseDto.getRefundId()).isEqualTo(1L);
        assertThat(refundPurchaseResponseDto.getReason()).isEqualTo(RefundReason.DEFEITO);

        assertThat(updatedPurchase.getStatus()).isEqualTo(StatusOrder.REEMBOLSADO);
    }

    @Test
    void refund_throwUnprocessedEntity_whenReasonIsDefeitoAndDateOrderIsOver90Days() throws Exception {
        // Given
        LocalDate invalidDateOrder = LocalDate.now().minusDays(91);
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.FINALIZADO, invalidDateOrder);
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                RefundReason.DEFEITO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void refund_throwUnprocessedEntity_whenReasonIsArrependimentoAndDateOrderIsOver7Days() throws Exception {
        // Given
        LocalDate invalidDateOrder = LocalDate.now().minusDays(8);
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.FINALIZADO, invalidDateOrder);
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                RefundReason.ARREPENDIMENTO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void refund_throwBadRequest_whenPurchaseIdIsNull() throws Exception {
        // Given
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                null,
                RefundReason.DEFEITO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refund_throwBadRequest_whenPurchaseIdIsNegative() throws Exception {
        // Given
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                -5L,
                RefundReason.DEFEITO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refund_throwNotFound_whenOrderPurchaseDoesNotExists() throws Exception {
        // Given
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                1L,
                RefundReason.DEFEITO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void refund_throwNotFound_whenOrderPurchaseStatusIsDifferentFromFinalizado() throws Exception {
        // Given
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.ABERTO, LocalDate.now());
        RefundPurchaseDto refundPurchaseDto = new RefundPurchaseDto(
                orderPurchase.getId(),
                RefundReason.DEFEITO
        );

        // When / Then
        mockMvc.perform(post("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPurchaseDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listRefunds_returnAllRefunds_whenItExistsAndNoParameterIsProvided() throws Exception {
        // Given
        OrderPurchaseRefund orderPurchaseRefund = createOrderPurchaseRefund();

        // When
        MvcResult result = mockMvc.perform(get("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ListRefundDto> listRefundDtos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        // Then
        assertThat(listRefundDtos).isNotEmpty();

        assertThat(listRefundDtos.get(0).getId()).isEqualTo(orderPurchaseRefund.getId());
    }

    @Test
    void listRefunds_returnRefundsFilteredByBuyer_whenItExistsBuyerIdIsProvided() throws Exception {
        // Given
        OrderPurchaseRefund orderPurchaseRefund = createOrderPurchaseRefund();
        createOrderPurchaseRefund();
        Long buyerId = orderPurchaseRefund.getOrderPurchase().getBuyer().getId();
        // When
        MvcResult result = mockMvc.perform(get("/api/order-purchase-refund?buyerId=" + buyerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ListRefundDto> listRefundDtos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        OrderPurchase foundOrderPurchase = orderPurchaseRepo.findById(listRefundDtos.get(0).getPurchaseId()).get();

        // Then
        assertThat(listRefundDtos).isNotEmpty();

        assertThat(foundOrderPurchase.getBuyer().getId()).isEqualTo(buyerId);
    }

    @Test
    void listRefunds_returnEmptyList_whenItDoesNotExists() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/order-purchase-refund")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ListRefundDto> listRefundDtos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        // Then
        assertThat(listRefundDtos).isEmpty();
    }

    @Test
    void getRefundById_getOrderPurchaseRefundDto_whenItExists() throws Exception {
        // Given
        OrderPurchaseRefund orderPurchaseRefund = createOrderPurchaseRefund();

        // When
        MvcResult result = mockMvc.perform(get("/api/order-purchase-refund/" + orderPurchaseRefund.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderPurchaseRefundDto orderPurchaseRefundDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrderPurchaseRefundDto.class
        );

        // Then
        assertThat(orderPurchaseRefundDto).isNotNull();
    }

    @Test
    void getRefundById_throwNotFound_whenItDoesNotExists() throws Exception {
        // When / Yhen
        mockMvc.perform(get("/api/order-purchase-refund/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private OrderPurchaseRefund createOrderPurchaseRefund() {
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        OrderPurchase orderPurchase = createOrderPurchase(itemBatches, StatusOrder.FINALIZADO, LocalDate.now());
        return orderPurchaseRefundRepo.save(
                new OrderPurchaseRefund(null, orderPurchase, RefundReason.ARREPENDIMENTO, LocalDate.now())
        );
    }

    private OrderPurchase createOrderPurchase(List<ItemBatch> itemBatches, StatusOrder statusOrder, LocalDate dateOrder) {
        Buyer buyer = createBuyer();
        OrderPurchase orderPurchase = orderPurchaseRepo.save(
                new OrderPurchase(null, buyer, dateOrder, List.of(), statusOrder)
        );
        ProductOrder productOrder1 = new ProductOrder(null, orderPurchase, itemBatches.get(0).getProduct(), 2);
        ProductOrder productOrder2 = new ProductOrder(null, orderPurchase, itemBatches.get(1).getProduct(), 1);
        orderPurchase.setProductOrders(List.of(productOrder1, productOrder2));

        return orderPurchaseRepo.save(orderPurchase);
    }

    private List<ItemBatch> createProductsAndItemBatches() {
        Product product1 = productRepo.save(new Product(1L, BigDecimal.valueOf(10), Category.FRESCO, null));
        Product product2 = productRepo.save(new Product(2L, BigDecimal.valueOf(5), Category.FRESCO, null));
        return itemBatchRepo.saveAll(
                List.of(
                    new ItemBatch(
                        1L,
                        product1,
                        10,
                        LocalDate.now(),
                        LocalDateTime.now(),
                        20L,
                        LocalDate.of(2022, 12, 14),
                        BigDecimal.valueOf(100),
                        null,
                        product1.getCategory()
                    ),
                    new ItemBatch(
                            2L,
                            product2,
                            5,
                            LocalDate.now(),
                            LocalDateTime.now(),
                            20L,
                            LocalDate.of(2022, 12, 14),
                            BigDecimal.valueOf(100),
                            null,
                            product2.getCategory()
                    )
                )
        );
    }

    private Buyer createBuyer() {
        return buyerRepo.save(new Buyer(null, List.of()));
    }
}
