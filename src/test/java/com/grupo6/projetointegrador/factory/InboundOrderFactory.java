package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Section;

import java.time.LocalDate;
import java.util.List;

public class InboundOrderFactory {
    private static Long id;
    private static List<ItemBatch> itemBatches;
    private static LocalDate orderDate;

    public static InboundOrder build(Section section) {
        InboundOrder inboundOrder = new InboundOrder();

        if (id == null) id = 1L;
        if (itemBatches == null || itemBatches.isEmpty()) itemBatches = List.of(ItemBatchFactory.build(inboundOrder));
        if (orderDate == null) orderDate = LocalDate.of(2020, 11, 9);

        inboundOrder.setWarehouse(section.getWarehouse());
        inboundOrder.setWarehouseOperator(section.getWarehouse().getWarehouseOperator());
        inboundOrder.setSection(section);

        inboundOrder.setId(id);
        inboundOrder.setItemBatches(itemBatches);
        inboundOrder.setOrderDate(orderDate);

        return inboundOrder;
    }
}
