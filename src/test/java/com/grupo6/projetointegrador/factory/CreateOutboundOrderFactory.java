package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.dto.CreateOutboundOrderDto;
import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.OutboundItemBatch;
import com.grupo6.projetointegrador.model.entity.OutboundOrder;

import java.time.LocalDate;
import java.util.List;

public class CreateOutboundOrderFactory {
    private static List<Long> itemBatches;

    private static LocalDate orderDate;

    public static CreateOutboundOrderDto build() {
        CreateOutboundOrderDto createdOutboundOrder = new CreateOutboundOrderDto();

        if (itemBatches == null || itemBatches.isEmpty()) itemBatches = List.of(1L);
        if (orderDate == null) orderDate = LocalDate.of(2020, 11, 9);
        createdOutboundOrder.setItemBatchIds(itemBatches);

        return createdOutboundOrder;
    }
}
