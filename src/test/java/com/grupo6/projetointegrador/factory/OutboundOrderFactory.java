package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.dto.OutboundOrderDto;
import com.grupo6.projetointegrador.model.entity.*;

import java.time.LocalDate;
import java.util.List;

public class OutboundOrderFactory {
    private static Long id;
    private static List<OutboundItemBatch> itemBatches;

    private static List<OutboundItemBatch> outboundItemBatches;

    private static LocalDate orderDate;

    public static OutboundOrder build() {
        OutboundOrder outboundOrder = new OutboundOrder();
        InboundOrder inboundOrder = new InboundOrder();

        if (id == null) id = 1L;
        if (itemBatches == null || itemBatches.isEmpty()) itemBatches = List.of(OutboundItemBatchFactory.build(outboundOrder, inboundOrder));
        if (orderDate == null) orderDate = LocalDate.of(2020, 11, 9);
        outboundOrder.setId(id);
        outboundOrder.setOutboundItemBatches(itemBatches);
        outboundOrder.setOrderDate(orderDate);

        return outboundOrder;
    }
}
