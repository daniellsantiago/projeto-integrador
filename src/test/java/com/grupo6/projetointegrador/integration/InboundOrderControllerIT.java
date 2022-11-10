package com.grupo6.projetointegrador.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.model.*;
import com.grupo6.projetointegrador.repository.*;
import com.grupo6.projetointegrador.service.InboundOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InboundOrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InboundOrderService inboundOrderService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private WarehouseOperatorRepo warehouseOperatorRepo;

    @Autowired
    private SectionRepo sectionRepo;

    @Test
    void createInboundOrder_createInboundOrder_whenAllProvidedDataIsValid() throws Exception {
        // Given
        createOperatorAndWarehouseAndSection(1L, 1L, 1L);
        createProductAndSeller(1L, 1L);
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                20L,
                LocalDate.now(),
                BigDecimal.valueOf(40),
                StorageType.FRESCO
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );

        // When
        ResultActions result = mockMvc.perform(post("/api/inboundorder")
                        .content(objectMapper.writeValueAsString(createInboundOrderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<ItemBatchDto> createdItemDtos = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        // Then
        assertThat(createdItemDtos).isNotEmpty();

        assertThat(createdItemDtos.get(0).getItemBatchId()).isEqualTo(1L);
        assertThat(createdItemDtos.get(0).getPrice()).isEqualTo(createItemBatchDto.getPrice());
        assertThat(createdItemDtos.get(0).getVolume()).isEqualTo(createItemBatchDto.getVolume());
        assertThat(createdItemDtos.get(0).getProductId()).isEqualTo(createItemBatchDto.getProductId());
        assertThat(createdItemDtos.get(0).getDueDate()).isEqualTo(createItemBatchDto.getDueDate());
        assertThat(createdItemDtos.get(0).getManufacturingDate()).isEqualTo(createItemBatchDto.getManufacturingDate());
        assertThat(createdItemDtos.get(0).getManufacturingTime()).isEqualTo(createItemBatchDto.getManufacturingTime());
        assertThat(createdItemDtos.get(0).getProductQuantity()).isEqualTo(createItemBatchDto.getProductQuantity());
    }

    @Test
    void createInboundOrder_returns422_whenSectionVolumeIsNotAvailable() throws Exception {
        // Given
        createOperatorAndWarehouseAndSection(1L, 1L, 1L);
        createProductAndSeller(1L, 1L);
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                2000000L,
                LocalDate.now(),
                BigDecimal.valueOf(40),
                StorageType.FRESCO
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );

        // When / Then
        mockMvc.perform(post("/api/inboundorder")
                        .content(objectMapper.writeValueAsString(createInboundOrderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInboundOrder_returns404_whenInputIsInvalid() throws Exception {
        // given
        CreateItemBatchDto invalidCreateItemBatchDto = new CreateItemBatchDto(
                null,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                20L,
                LocalDate.now(),
                null,
                StorageType.FRESCO
        );
        CreateInboundOrderDto invalidCreateInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(invalidCreateItemBatchDto)
        );

        // when / Then
        mockMvc.perform(post("/api/inboundorder")
                        .content(objectMapper.writeValueAsString(invalidCreateInboundOrderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createInboundOrder_returns400_whenProductDoesNotExists() throws Exception {
        // given
        createOperatorAndWarehouseAndSection(1L, 1L, 1L);
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                20L,
                LocalDate.now(),
                BigDecimal.valueOf(40),
                StorageType.FRESCO
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );

        // when / Then
        mockMvc.perform(post("/api/inboundorder")
                        .content(objectMapper.writeValueAsString(createInboundOrderDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemBatches_updateAnItemAndCreateOne_whenAllProvidedDataIsValid() throws Exception {
        // Given
        createOperatorAndWarehouseAndSection(1L, 1L, 1L);
        createProductAndSeller(1L, 1L);
        createInboundWithOneItem();
        UpdateItemBatchDto updateExistingItemDto = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );
        UpdateItemBatchDto createItemDto = new UpdateItemBatchDto(
                null,
                1L,
                10,
                LocalDate.of(2022, 10, 20),
                LocalDateTime.of(2022, 10, 20, 1, 30, 10),
                6L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(23)
        );
        List<UpdateItemBatchDto> updateItemBatchDto = List.of(updateExistingItemDto, createItemDto);

        // When
        ResultActions result = mockMvc.perform(put("/api/inboundorder/1/item-batch")
                        .content(objectMapper.writeValueAsString(updateItemBatchDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<ItemBatchDto> updatedItemDtos = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        ItemBatchDto updatedItemBatchDto = updatedItemDtos.stream()
                .filter(dto -> dto.getItemBatchId().equals(1L)).findFirst().get();
        ItemBatchDto createdItemBatchDto = updatedItemDtos.stream()
                .filter(dto -> dto.getItemBatchId().equals(2L)).findFirst().get();

        // Then
        assertThat(updatedItemDtos).isNotEmpty();

        assertThat(updatedItemBatchDto.getItemBatchId()).isEqualTo(updateExistingItemDto.getItemBatchId());
        assertThat(updatedItemBatchDto.getPrice()).isEqualTo(updateExistingItemDto.getPrice());
        assertThat(updatedItemBatchDto.getVolume()).isEqualTo(updateExistingItemDto.getVolume());
        assertThat(updatedItemBatchDto.getProductId()).isEqualTo(updateExistingItemDto.getProductId());
        assertThat(updatedItemBatchDto.getDueDate()).isEqualTo(updateExistingItemDto.getDueDate());
        assertThat(updatedItemBatchDto.getManufacturingDate()).isEqualTo(updateExistingItemDto.getManufacturingDate());
        assertThat(updatedItemBatchDto.getManufacturingTime()).isEqualTo(updateExistingItemDto.getManufacturingTime());
        assertThat(updatedItemBatchDto.getProductQuantity()).isEqualTo(updateExistingItemDto.getProductQuantity());

        assertThat(createdItemBatchDto.getItemBatchId()).isEqualTo(2L);
        assertThat(createdItemBatchDto.getPrice()).isEqualTo(createItemDto.getPrice());
        assertThat(createdItemBatchDto.getVolume()).isEqualTo(createItemDto.getVolume());
        assertThat(createdItemBatchDto.getProductId()).isEqualTo(createItemDto.getProductId());
        assertThat(createdItemBatchDto.getDueDate()).isEqualTo(createItemDto.getDueDate());
        assertThat(createdItemBatchDto.getManufacturingDate()).isEqualTo(createItemDto.getManufacturingDate());
        assertThat(createdItemBatchDto.getManufacturingTime()).isEqualTo(createItemDto.getManufacturingTime());
        assertThat(createdItemBatchDto.getProductQuantity()).isEqualTo(createItemDto.getProductQuantity());
    }

    @Test
    void updateItemBatches_returns404_whenInputIsInvalid() throws Exception {
        // given
        UpdateItemBatchDto invalidDto = new UpdateItemBatchDto(
                1L,
                null,
                -5,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                null,
                BigDecimal.valueOf(50)
        );

        // when / Then
        mockMvc.perform(put("/api/inboundorder/1/item-batch")
                        .content(objectMapper.writeValueAsString(List.of(invalidDto)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemBatches_returns400_whenInboundOrderDoesNotExists() throws Exception {
        // given
        UpdateItemBatchDto validDTO = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );

        // when / Then
        mockMvc.perform(put("/api/inboundorder/212122/item-batch")
                        .content(objectMapper.writeValueAsString(List.of(validDTO)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private void createOperatorAndWarehouseAndSection(Long operatorId, Long warehouseId, Long sectionId) {
        WarehouseOperator warehouseOperator = warehouseOperatorRepo.save(new WarehouseOperator(operatorId, null));
        Section section = sectionRepo.save(new Section(sectionId, null, 2000L, StorageType.FRESCO));
        Warehouse warehouse = warehouseRepo.save(new Warehouse(warehouseId, List.of(section), warehouseOperator));
        section.setWarehouse(warehouse);
        sectionRepo.save(section);
        warehouse.setSections(List.of(section));
        warehouseRepo.save(warehouse);
    }

    private void createProductAndSeller(Long productId, Long sellerId) {
        Seller seller = sellerRepo.save(new Seller(sellerId, null));
        Product product = productRepo.save(new Product(productId, BigDecimal.valueOf(5), StorageType.FRESCO, seller));

        seller.setProducts(List.of(product));
        sellerRepo.save(seller);
    }

    private void createInboundWithOneItem() {
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.of(2022, 11, 10),
                LocalDateTime.of(2022, 11, 10, 4, 10, 30),
                20L,
                LocalDate.of(2023, 1, 20),
                BigDecimal.valueOf(5000),
                StorageType.FRESCO
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );
        inboundOrderService.createInboundOrder(createInboundOrderDto);
    }
}
