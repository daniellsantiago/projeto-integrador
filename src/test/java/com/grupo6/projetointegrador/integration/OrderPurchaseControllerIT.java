package com.grupo6.projetointegrador.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.ProductOrderDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderPurchaseControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private ItemBatchRepo itemBatchRepo;

    @Autowired
    private OrderPurchaseRepo orderPurchaseRepo;

    @Test
    void findOrderPurchase_getOrderPurchaseDto_whenProvidedOrderPurchaseIdExists() throws Exception {
        // Given
        createOrderPurchase(StatusOrder.ABERTO);

        // When
        ResultActions result = mockMvc.perform(get("/api/order-purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        OrderPurchaseDto orderPurchaseDto = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                OrderPurchaseDto.class
        );

        // Then
        assertThat(orderPurchaseDto).isNotNull();
        assertThat(orderPurchaseDto.getProductOrders().size()).isEqualTo(1);
        assertThat(orderPurchaseDto.getId()).isEqualTo(1);
        assertThat(orderPurchaseDto.getStatus()).isEqualTo(StatusOrder.ABERTO);
        assertThat(orderPurchaseDto.getDateOrder()).isEqualTo(LocalDate.now());
    }

    @Test
    void findOrderPurchase_throwsNotFound_whenOrderPurchaseDoesNotExists() throws Exception {
        mockMvc.perform(get("/api/order-purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void alterOrderPurchase_updateOrderStatus_whenProvidedOrderPurchaseExistsAndHasAbertoStatus() throws Exception {
        // Given
        createOrderPurchase(StatusOrder.ABERTO);

        // When
        mockMvc.perform(put("/api/order-purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        OrderPurchase orderPurchase = orderPurchaseRepo.findById(1L).get();

        // Then
        assertThat(orderPurchase).isNotNull();
        assertThat(orderPurchase.getStatus()).isEqualTo(StatusOrder.FINALIZADO);
    }

    @Test
    void alterOrderPurchase_throwsNotFoundException_whenProvidedOrderPurchaseDoesNotExists() throws Exception {
        mockMvc.perform(put("/api/order-purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void alterOrderPurchase_throwsUnprocessableEntity_whenProvidedOrderHasFinalizadoStatus() throws Exception {
        // Given
        createOrderPurchase(StatusOrder.FINALIZADO);

        // When / Then
        mockMvc.perform(put("/api/order-purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createOrderPurchase_createOrderAndReturnTotalPrice_whenProvidedDataIsValid() throws Exception {
        // Given
        createProductAndItemBatch();
        createBuyer();
        CreateOrderPurchaseDto createOrderPurchaseDto = new CreateOrderPurchaseDto(
                1L,
                LocalDate.now(),
                List.of(new ProductOrderDto(1L, 2))
        );
        // When
        ResultActions result = mockMvc.perform(post("/api/order-purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderPurchaseDto)))
                .andExpect(status().isCreated());
        TotalPriceDto totalPriceDto = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                TotalPriceDto.class
        );

        // Then
        assertThat(totalPriceDto).isNotNull();
        assertThat(totalPriceDto.getTotalPrice()).isEqualTo(20);
    }

    @Test
    void createOrderPurchase_throwsNotFound_whenBuyerDoesNotExists() throws Exception {
        // Given
        createProductAndItemBatch();
        CreateOrderPurchaseDto createOrderPurchaseDto = new CreateOrderPurchaseDto(
                1L,
                LocalDate.now(),
                List.of(new ProductOrderDto(1L, 2))
        );
        // When / Then
        mockMvc.perform(post("/api/order-purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderPurchaseDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrderPurchase_throwsBadRequest_whenInputDataIsNotValid() throws Exception {
        // Given
        CreateOrderPurchaseDto createOrderPurchaseDto = new CreateOrderPurchaseDto(
                1L,
                LocalDate.now(),
                List.of(new ProductOrderDto(null, 2))
        );
        // When / Then
        mockMvc.perform(post("/api/order-purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderPurchaseDto)))
                .andExpect(status().isBadRequest());
    }

    private void createOrderPurchase(StatusOrder statusOrder) {
        ItemBatch itemBatch = createProductAndItemBatch();
        Buyer buyer = createBuyer();
        OrderPurchase orderPurchase = orderPurchaseRepo.save(
                new OrderPurchase(null, buyer, LocalDate.now(), null, statusOrder)
        );
        ProductOrder productOrder = new ProductOrder(null, orderPurchase, itemBatch.getProduct(), 2);
        orderPurchase.setProductOrders(List.of(productOrder));
        orderPurchaseRepo.save(orderPurchase);
    }

    private ItemBatch createProductAndItemBatch() {
        Product product = productRepo.save(new Product(1L, BigDecimal.valueOf(10), Category.FRESCO, null));
        return itemBatchRepo.save(new ItemBatch(
                1L,
                product,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                20L,
                LocalDate.of(2022, 12, 14),
                BigDecimal.valueOf(100),
                null,
                product.getCategory()
        ));
    }

    private Buyer createBuyer() {
        return buyerRepo.save(new Buyer(1L, List.of()));
    }
}
