package com.grupo6.projetointegrador.integration;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
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
