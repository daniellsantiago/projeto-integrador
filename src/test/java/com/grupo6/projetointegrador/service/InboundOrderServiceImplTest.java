package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.*;
import com.grupo6.projetointegrador.repository.InboundOrderRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.repository.WarehouseOperatorRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceImplTest {
    @Mock
    private InboundOrderRepo inboundOrderRepo;

    @Mock
    private WarehouseRepo warehouseRepo;

    @Mock
    private WarehouseOperatorRepo warehouseOperatorRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private InboundOrderServiceImpl inboundOrderService;

    @Test
    void updateItemBatch_changeItemAndCreateOne_whenInboundAndProductExists() {
        // Given
        List<Product> products = setupSellerContainingTwoProducts().getProducts();
        InboundOrder savedInboundOrder = setupGenericInboundOrder();
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );
        UpdateItemBatchDto createFeijaoItemBatch = new UpdateItemBatchDto(
                null,
                2L,
                5,
                LocalDate.of(2021, 8, 20),
                LocalDateTime.of(2021, 3, 20, 1, 30, 10),
                2L,
                LocalDate.of(2020, 11, 20),
                BigDecimal.valueOf(60)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedInboundOrder));
        Mockito.when(productRepo.findById(1L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(1L)).findFirst());
        Mockito.when(productRepo.findById(2L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(2L)).findFirst());

        // Then
        List<ItemBatchDto> result = inboundOrderService.updateItemBatch(
                1L,
                List.of(updateArrozItemBatch, createFeijaoItemBatch)
        );
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getItemBatchId()).isEqualTo(updateArrozItemBatch.getItemBatchId());
        assertThat(result.get(0).getPrice()).isEqualTo(updateArrozItemBatch.getPrice());
        assertThat(result.get(0).getProductId()).isEqualTo(updateArrozItemBatch.getProductId());
        assertThat(result.get(0).getVolume()).isEqualTo(updateArrozItemBatch.getVolume());
        assertThat(result.get(0).getDueDate()).isEqualTo(updateArrozItemBatch.getDueDate());
        assertThat(result.get(0).getManufacturingDate()).isEqualTo(updateArrozItemBatch.getManufacturingDate());

        assertThat(result.get(1).getPrice()).isEqualTo(createFeijaoItemBatch.getPrice());
        assertThat(result.get(1).getProductId()).isEqualTo(createFeijaoItemBatch.getProductId());
        assertThat(result.get(1).getVolume()).isEqualTo(createFeijaoItemBatch.getVolume());
        assertThat(result.get(1).getDueDate()).isEqualTo(createFeijaoItemBatch.getDueDate());
        assertThat(result.get(1).getManufacturingDate()).isEqualTo(createFeijaoItemBatch.getManufacturingDate());
    }

    @Test
    void updateItemBatch_throwsNotFoundException_whenInboundOrderDoesNotExists() {
        // Given
        UpdateItemBatchDto dto = new UpdateItemBatchDto();

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(dto))
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateItemBatch_throwsNotFoundException_whenProductDoesNotExists() {
        // Given
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(setupGenericInboundOrder()));

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(updateArrozItemBatch))
        ).isInstanceOf(NotFoundException.class);
    }

    private InboundOrder setupGenericInboundOrder() {
        WarehouseOperator warehouseOperator = new WarehouseOperator(1L, null);
        Section section = new Section(1L, null, 2000L, StorageType.FRESCO);
        Warehouse warehouse = new Warehouse(1L, List.of(section), warehouseOperator);
        section.setWarehouse(warehouse);

        InboundOrder inboundOrder = new InboundOrder(1L, warehouseOperator, section, null, warehouse, LocalDate.of(2020, 11, 9));

        Seller seller = setupSellerContainingTwoProducts();

        ItemBatch arrozItemBatch = new ItemBatch(
                1L,
                seller.getProducts().get(0),
                10,
                LocalDate.of(2022, 10, 20),
                LocalDateTime.of(2022, 10, 20, 1, 30, 10),
                10L,
                LocalDate.of(2022, 11, 20),
                BigDecimal.valueOf(80),
                inboundOrder,
                StorageType.FRESCO
        );

        inboundOrder.setItemBatches(List.of(arrozItemBatch));

        return inboundOrder;
    }

    private Seller setupSellerContainingTwoProducts() {
        Seller seller = new Seller(1L, null);

        Product arroz = new Product(1L, BigDecimal.valueOf(5), StorageType.FRESCO, seller);
        Product feijao = new Product(2L, BigDecimal.valueOf(8), StorageType.FRESCO, seller);

        seller.setProducts(List.of(arroz, feijao));
        return seller;
    }
}
