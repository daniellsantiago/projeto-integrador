package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.*;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.*;
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
public class OutboundOrderServiceImplTest {
    @Mock
    private OutboundOrderRepo outboundOrderRepo;

    @Mock
    private ItemBatchRepo itemBatchRepo;

    @Mock
    private OutboundItemBatchesRepo outboundItemBatchesRepo;

    @InjectMocks
    private OutboundOrderServiceImpl outboundOrderService;


    @Test
    void createOutboundOrder() {
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        InboundOrder inboundOrder = InboundOrderFactory.build(section);
        OutboundOrderDto outboundOrderDto = OutboundOrderDto.fromInboundOrder(inboundOrder);

        Mockito.when(outboundOrderRepo.save(ArgumentMatchers.any(OutboundOrder.class))).thenReturn(OutboundOrderFactory.build());
        Mockito.when(itemBatchRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ItemBatchFactory.build(inboundOrder)));

        OutboundOrderDto outboundOrder = outboundOrderService.createOutboundOrder(CreateOutboundOrderFactory.build());
        assertThat(outboundOrder).isNotNull();
        assertThat(outboundOrder.getItemBatches()).isNotEmpty();
    }

    @Test
    void getAllRetornaListaDeOutboundOrders() {
        Mockito.when(outboundOrderRepo.findAll()).thenReturn(List.of(OutboundOrderFactory.build()));
        List<OutboundOrderDto> outboundOrders = outboundOrderService.getAll();
        assertThat(outboundOrders).isNotNull();
        assertThat(outboundOrders).isNotEmpty();
        assertThat(outboundOrders).hasSize(1);
    }
}
