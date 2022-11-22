package com.grupo6.projetointegrador.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.PatchItemBatchDto;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Section;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.*;
import com.grupo6.projetointegrador.service.InboundOrderService;
import com.grupo6.projetointegrador.service.ItemBatchService;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemBatchControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WarehouseOperatorRepo warehouseOperatorRepo;
    @Autowired
    private WarehouseRepo warehouseRepo;
    @Autowired
    private SectionRepo sectionRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private InboundOrderService inboundOrderService;
    @Autowired
    private ItemBatchService itemBatchService;


    @Test
    void patchItemBatch_updateItemBatch_whenItemBatchExistsAndProvidedValidBody() throws Exception {

        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        createInboundWithOneItem();

        Long itemBatchId = 1L;

        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(1, 1L, BigDecimal.valueOf(1.0));

        ResultActions result = mockMvc.perform(patch("/api/item-batch?itemBatchId=" + itemBatchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchItemBatchDto)))
                .andExpect(status().isOk());

        ItemBatchDto itemBatchDto = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(),
                ItemBatchDto.class);

        assertThat(itemBatchDto).isNotNull();

        assertThat(itemBatchDto.getProductQuantity()).isEqualTo(patchItemBatchDto.getProductQuantity());
        assertThat(itemBatchDto.getVolume()).isEqualTo(patchItemBatchDto.getVolume());
        assertThat(itemBatchDto.getPrice()).isEqualTo(patchItemBatchDto.getPrice());
    }

    @Test
    void patchItemBatch_returns404_whenItemBatchDoesNotExist() throws Exception{
        Long itemBatchId = 1L;

        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(1, 1L, BigDecimal.valueOf(1.0));

        mockMvc.perform(patch("/api/item-batch?itemBatchId=" + itemBatchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItemBatchDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchItemBatch_returns422_whenProductQuantityLessThanOrEqualsZero() throws Exception{
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        createInboundWithOneItem();

        Long itemBatchId = 1L;

        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(-1, null, null);

        mockMvc.perform(patch("/api/item-batch?itemBatchId=" + itemBatchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItemBatchDto)))
                        .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void patchItemBatch_returns422_whenVolumeLessThanOrEqualsZero() throws Exception{
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        createInboundWithOneItem();

        Long itemBatchId = 1L;

        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(null, -1L, null);

        mockMvc.perform(patch("/api/item-batch?itemBatchId=" + itemBatchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItemBatchDto)))
                .andExpect(status().isUnprocessableEntity());

    }
    @Test
    void patchItemBatch_returns422_whenPriceLessThanOrEqualsZero() throws Exception{
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        createInboundWithOneItem();

        Long itemBatchId = 1L;

        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(null, null, BigDecimal.valueOf(-1.0));

        mockMvc.perform(patch("/api/item-batch?itemBatchId=" + itemBatchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItemBatchDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getItemBatchesOutOfDate_returnItemBatchList_whenItemBatchIsOutOfDate() throws Exception{
        // Given
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.of(2022, 11, 10),
                LocalDateTime.of(2022, 11, 10, 4, 10, 30),
                20L,
                LocalDate.now(),
                BigDecimal.valueOf(5000)
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );
        inboundOrderService.createInboundOrder(createInboundOrderDto);

        // When
        ResultActions result = mockMvc.perform(get("/api/item-batch/audit/dueDate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<ItemBatchDto> itemBatchDtoList = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        // Then
        assertThat(itemBatchDtoList).isNotEmpty();

        assertThat(itemBatchDtoList.get(0).getDueDate()).isBeforeOrEqualTo(LocalDate.now().plusDays(21));
    }

    @Test
    void getItemBatchesOutOfDate_return404_whenEveryItemBatchIsWithinTheValidity() throws Exception{
        // Given
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.of(2022, 11, 10),
                LocalDateTime.of(2022, 11, 10, 4, 10, 30),
                20L,
                LocalDate.now().plusDays(22),
                BigDecimal.valueOf(5000)
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );
        inboundOrderService.createInboundOrder(createInboundOrderDto);

        // When Then
        mockMvc.perform(get("/api/item-batch/audit/dueDate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getChangedBetweenTwoDates_returnItemBatchDtoList_whenChangeDateTimeIsWithinThePeriod() throws Exception{
        // Given
        createOperatorAndWarehouseAndSection();
        createProductAndSeller(1L, 1L);
        createInboundWithTwoItems();
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(1, 1L, BigDecimal.valueOf(1.0));

        itemBatchService.patchItemBatch(1L, patchItemBatchDto);

        String strLocalDateYesterday = LocalDate.now().minusDays(1).toString();
        String strLocalDateTomorrow = LocalDate.now().plusDays(1).toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime yesterday = LocalDate.parse(strLocalDateYesterday, formatter).atStartOfDay();
        LocalDateTime tomorrow = LocalDate.parse(strLocalDateTomorrow, formatter).atStartOfDay();

        //When
        ResultActions result = mockMvc.perform(get("/api/item-batch/audit/changes")
                        .param("dateMin", strLocalDateYesterday)
                        .param("dateMax", strLocalDateTomorrow)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<ItemBatchDto> itemBatchDtoList = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        // Then
        assertThat(itemBatchDtoList).isNotEmpty();

        assertThat(itemBatchDtoList.get(0).getLastChangeDateTime()).isBetween(yesterday, tomorrow);
    }

    private void createInboundWithTwoItems() {
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.of(2022, 11, 10),
                LocalDateTime.of(2022, 11, 10, 4, 10, 30),
                20L,
                LocalDate.now().plusDays(23),
                BigDecimal.valueOf(5000)
        );
        CreateItemBatchDto createItemBatchDto2 = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.of(2022, 11, 10),
                LocalDateTime.of(2022, 11, 10, 4, 10, 30),
                20L,
                LocalDate.now().plusDays(23),
                BigDecimal.valueOf(5000)
        );
        CreateInboundOrderDto createInboundOrderDto = new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto, createItemBatchDto2)
        );
        inboundOrderService.createInboundOrder(createInboundOrderDto);
    }

    private void createOperatorAndWarehouseAndSection() {
        Warehouse warehouse = WarehouseFactory.build();
        List<Section> sections = warehouse.getSections();

        warehouseOperatorRepo.save(warehouse.getWarehouseOperator());

        warehouse.setSections(List.of());
        warehouseRepo.save(warehouse);

        sectionRepo.saveAll(sections);

        warehouse.setSections(sections);
        warehouseRepo.save(warehouse);
    }

    private void createProductAndSeller(Long productId, Long sellerId) {
        Seller seller = sellerRepo.save(new Seller(sellerId, null));
        Product product = productRepo.save(new Product(productId, BigDecimal.valueOf(5), Category.FRESCO, seller));

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
                BigDecimal.valueOf(5000)
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
