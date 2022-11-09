package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.Warehouse;
import com.grupo6.projetointegrador.model.WarehouseOperator;
import com.grupo6.projetointegrador.repository.InboundOrderRepo;
import com.grupo6.projetointegrador.repository.WarehouseOperatorRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundOrderServiceImpl implements InboundOrderService{

    private InboundOrderRepo inboundOrderRepo;
    private WarehouseRepo warehouseRepo;

    private WarehouseOperatorRepo warehouseOperatorRepo;

    public InboundOrderServiceImpl(InboundOrderRepo inboundOrderRepo, WarehouseRepo warehouseRepo, WarehouseOperatorRepo warehouseOperatorRepo) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
    }

    @Override
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {

    }

    private Warehouse checkWarehouseId(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator checkWarehouseOperator(Long warehouseOperatorId, Long warehouseId){
        WarehouseOperator operator = warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
        if(checkWarehouseId(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)){
            return operator;
        } else {
            throw new
        }
    }
}
