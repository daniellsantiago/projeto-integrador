package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutboundOrderServiceImpl implements OutboundOrderService {

    private final OutboundOrderRepo outboundOrderRepo;

    private final ItemBatchRepo itemBatchRepo;

    public OutboundOrderServiceImpl(
            OutboundOrderRepo outboundOrderRepo,
            WarehouseRepo warehouseRepo,
            WarehouseOperatorRepo warehouseOperatorRepo,
            ProductRepo productRepo,
            SectionRepo sectionRepo,
            InboundOrderRepo inboundOrderRepo,
            ItemBatchRepo itemBatchRepo
    ) {
        this.outboundOrderRepo = outboundOrderRepo;
        this.itemBatchRepo = itemBatchRepo;
    }

    /**
     * This method receives a DTO with a list of items and ids.
     * @param createOutboundOrderDto This is the object that will be sent by the frontend.
     * @return A List<OutboundItemBatchDto> object with the stored items.
     */

    /**

     */

    @Override
    @Transactional
    public OutboundOrderDto createOutboundOrder(CreateOutboundOrderDto createOutboundOrderDto) {
        OutboundOrder newOutboundOrder = new OutboundOrder();
        newOutboundOrder.setOrderDate(LocalDate.now());
        outboundOrderRepo.save(newOutboundOrder);
        List<OutboundItemBatch> batchList = createOutboundOrderDto.getItemBatchIds().stream().map((id) -> {
            ItemBatch itemBatch = itemBatchRepo.findById(id).orElseThrow(() -> new NotFoundException("ItemBatch com o id '" + id + "' não encontrado"));
            InboundOrder inboundOrder = itemBatch.getInboundOrder();
            itemBatchRepo.deleteById(itemBatch.getId());
            return new OutboundItemBatch(itemBatch, inboundOrder, newOutboundOrder);
        }).collect(Collectors.toList());
        newOutboundOrder.setOutboundItemBatches(batchList);
        return OutboundOrderDto.fromOutboundOrder(newOutboundOrder);
    }

    /**
      * This method receives a DTO with a list of items and ids.
      * The main goal is to store those items on the provided section.
      * @return A List<OutboundItemBatchDto> object with the stored items.
      */
    @Override
    public List<OutboundOrderDto> getAll() {
        return outboundOrderRepo.findAll().stream()
                .map((OutboundOrderDto::fromOutboundOrder))
                .collect(Collectors.toList());
    }
}
