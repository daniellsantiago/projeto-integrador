package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.PatchItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.ItemBatchFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ItemBatchServiceImplTest {

    @Mock
    private ItemBatchRepo itemBatchRepo;

    @InjectMocks
    private ItemBatchServiceImpl itemBatchService;

    @Test
    void getItemBatchesOutOfDate_returnItemBatchDtoList_whenExistsItemBatchOutOfDate() {
        //Given
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        List<ItemBatch> itemBatches = List.of(ItemBatchFactory.build(inboundOrder));

        //When
        Mockito.when(itemBatchRepo.findAllOutOfDate()).thenReturn(itemBatches);
        List<ItemBatchDto> itemBatchDtoList = itemBatchService.getItemBatchesOutOfDate();

        // Then
        assertThat(itemBatchDtoList).isNotEmpty();

        assertThat(itemBatchDtoList.get(0).getItemBatchId()).isEqualTo(1L);
        assertThat(itemBatchDtoList.get(0).getDueDate()).isEqualTo(LocalDate.of(2022, 11, 20));
        assertThat(itemBatchDtoList.get(0).getVolume()).isEqualTo(10L);
        assertThat(itemBatchDtoList.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(50));
        assertThat(itemBatchDtoList.get(0).getProductQuantity()).isEqualTo(10);
        assertThat(itemBatchDtoList.get(0).getManufacturingDate()).isEqualTo(LocalDate.of(2022, 10, 20));
        assertThat(itemBatchDtoList.get(0).getManufacturingTime()).isEqualTo(LocalDateTime.of(2022, 10, 20, 1, 30, 10));


    }
    @Test
    void getItemBatchesOutOfDate_throwNotFoundException_whenDoesNotExistItemBatchOutOfDate() {
        //When
        Mockito.when(itemBatchRepo.findAllOutOfDate()).thenReturn(List.of());

        // Then
        assertThatThrownBy(() -> itemBatchService.getItemBatchesOutOfDate())
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void patchItemBatch_updateItemBatch_whenProvidedValidBodyAndValidItemBatchId() {
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(1, 1L, BigDecimal.valueOf(1.0));

        Mockito.when(itemBatchRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(itemBatch));
        Mockito.when(itemBatchRepo.save(Mockito.any())).thenReturn(itemBatch);

        ItemBatchDto itemBatchDto = itemBatchService.patchItemBatch(1L, patchItemBatchDto);

        assertThat(itemBatchDto).isNotNull();
        assertThat(itemBatchDto.getVolume()).isEqualTo(patchItemBatchDto.getVolume());
        assertThat(itemBatchDto.getPrice()).isEqualTo(patchItemBatchDto.getPrice());
        assertThat(itemBatchDto.getProductQuantity()).isEqualTo(patchItemBatchDto.getProductQuantity());
    }

    @Test
    void patchItemBatch_throwNotFoundException_whenProvidedInvalidItemBatchId() {
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(1, 1L, BigDecimal.valueOf(1.0));

        Mockito.when(itemBatchRepo.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemBatchService.patchItemBatch( 1L, patchItemBatchDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void patchItemBatch_throwBusinessRuleException_whenProvidedInvalidProductQuantity() {
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(-1, null, null);

        Mockito.when(itemBatchRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(itemBatch));

        assertThatThrownBy(() -> itemBatchService.patchItemBatch( 1L, patchItemBatchDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void patchItemBatch_throwBusinessRuleException_whenProvidedInvalidVolume() {
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(null, -1L, null);

        Mockito.when(itemBatchRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(itemBatch));

        assertThatThrownBy(() -> itemBatchService.patchItemBatch( 1L, patchItemBatchDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void patchItemBatch_throwBusinessRuleException_whenProvidedInvalidPrice() {
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        PatchItemBatchDto patchItemBatchDto = new PatchItemBatchDto(null, null, BigDecimal.valueOf(-1.0));

        Mockito.when(itemBatchRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(itemBatch));

        assertThatThrownBy(() -> itemBatchService.patchItemBatch( 1L, patchItemBatchDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void getChangedBetweenTwoDates_returnItemBatchDtoList_whenProvidedValidDateMinAndDateMax() {
        //Given
        Warehouse warehouse = WarehouseFactory.build();
        InboundOrder inboundOrder = InboundOrderFactory.build(warehouse.getSections().get(0));
        ItemBatch itemBatch = ItemBatchFactory.build(inboundOrder);
        itemBatch.setLastChangeDateTime(LocalDateTime.now());
        List<ItemBatch> itemBatchList = List.of(itemBatch);

        String strLocalDateYesterday = LocalDate.now().minusDays(1).toString();
        String strLocalDateTomorrow = LocalDate.now().plusDays(1).toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime yesterday = LocalDate.parse(strLocalDateYesterday, formatter).atStartOfDay();
        LocalDateTime tomorrow = LocalDate.parse(strLocalDateTomorrow, formatter).atStartOfDay();

        //When
        Mockito.when(itemBatchRepo.findAllBetweenTwoDates(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(itemBatchList));
        List<ItemBatchDto> result = itemBatchService.getChangedBetweenTwoDates(strLocalDateYesterday, strLocalDateTomorrow);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLastChangeDateTime()).isBetween(yesterday, tomorrow);

        assertThat(result.get(0).getItemBatchId()).isEqualTo(1L);
        assertThat(result.get(0).getDueDate()).isEqualTo(LocalDate.of(2022, 11, 20));
        assertThat(result.get(0).getVolume()).isEqualTo(10L);
        assertThat(result.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(50));
        assertThat(result.get(0).getProductQuantity()).isEqualTo(10);
        assertThat(result.get(0).getManufacturingDate()).isEqualTo(LocalDate.of(2022, 10, 20));
        assertThat(result.get(0).getManufacturingTime()).isEqualTo(LocalDateTime.of(2022, 10, 20, 1, 30, 10));
    }

    @Test
    void getChangedBetweenTwoDates_throwNotFoundException_whenThereIsNoItemBatchBetweenDateMinAndDateMax() {
        //Given
        String strLocalDateYesterday = LocalDate.now().minusDays(1).toString();
        String strLocalDateTomorrow = LocalDate.now().plusDays(1).toString();

        //When
        Mockito.when(itemBatchRepo.findAllBetweenTwoDates(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemBatchService.getChangedBetweenTwoDates(strLocalDateYesterday, strLocalDateTomorrow))
                .isInstanceOf(NotFoundException.class);
        }
}