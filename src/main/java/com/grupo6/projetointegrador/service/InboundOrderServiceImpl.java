package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.InboundOrder;
import com.grupo6.projetointegrador.model.ItemBatch;
import com.grupo6.projetointegrador.model.Warehouse;
import com.grupo6.projetointegrador.model.WarehouseOperator;
import com.grupo6.projetointegrador.repository.InboundOrderRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.repository.WarehouseOperatorRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderServiceImpl implements InboundOrderService{

    private final InboundOrderRepo inboundOrderRepo;
    private final WarehouseRepo warehouseRepo;

    private final WarehouseOperatorRepo warehouseOperatorRepo;

    private final ProductRepo productRepo;

    public InboundOrderServiceImpl(
            InboundOrderRepo inboundOrderRepo,
            WarehouseRepo warehouseRepo,
            WarehouseOperatorRepo warehouseOperatorRepo,
            ProductRepo productRepo
    ) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {
        return null;
    }

    @Override
    public List<ItemBatchDto> updateItemBatch(Long inboundOrderId, List<UpdateItemBatchDto> updateItemBatchDtos) {
        InboundOrder inboundOrder = inboundOrderRepo.findById(inboundOrderId)
                .orElseThrow(() -> new NotFoundException("InboundOrder não encontrado"));
        List<ItemBatch> updatedItemBatches = updateItemBatchDtos.stream()
                .map(itemBatchDto -> itemBatchDto.toItemBatch(
                        inboundOrder,
                        productRepo.findById(itemBatchDto.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"))
                ))
                .collect(Collectors.toList());
        inboundOrder.setItemBatches(updatedItemBatches);
        inboundOrderRepo.save(inboundOrder);
        return updatedItemBatches.stream()
                .map(ItemBatchDto::fromItemBatch)
                .collect(Collectors.toList());
    }

    private Warehouse checkWarehouseId(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator checkWarehouseOperator(Long warehouseOperatorId, Long warehouseId){
        WarehouseOperator operator = warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
        if(checkWarehouseId(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)){
            return operator;
        } else {
            return null;
        }
    }
}
