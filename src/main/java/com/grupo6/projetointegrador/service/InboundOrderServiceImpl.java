package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.Section;
import com.grupo6.projetointegrador.model.Warehouse;
import com.grupo6.projetointegrador.model.WarehouseOperator;
import com.grupo6.projetointegrador.repository.InboundOrderRepo;
import com.grupo6.projetointegrador.repository.SectionRepo;
import com.grupo6.projetointegrador.repository.WarehouseOperatorRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundOrderServiceImpl implements InboundOrderService{

    private InboundOrderRepo inboundOrderRepo;
    private WarehouseRepo warehouseRepo;
    private WarehouseOperatorRepo warehouseOperatorRepo;
    private SectionRepo sectionRepo;

    public InboundOrderServiceImpl(InboundOrderRepo inboundOrderRepo, WarehouseRepo warehouseRepo, WarehouseOperatorRepo warehouseOperatorRepo, SectionRepo sectionRepo) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.sectionRepo = sectionRepo;
    }

    @Override
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {
        return null;
    }

    private Warehouse findOrThrowWarehouse(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator findOrThrowWarehouseOperator(Long warehouseOperatorId, Long warehouseId){
        WarehouseOperator operator = warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));

        if(findOrThrowWarehouse(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)){
            return operator;
        } else {
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }

    private Section findOrThrowSection(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }

    private Section verifyWarehouseMatchSection(Long sectionId, Long warehouseId){
        Section section = findOrThrowSection(sectionId);
        if(section.getWarehouse().getId().equals(warehouseId)){
            return section;
        } else {
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    private boolean matchStorageType(CreateItemBatchDto itemBatchDto, Long sectionId){
        Section section = findOrThrowSection(sectionId);

        return section.getStorageType().equals(itemBatchDto.getStorageType());
    }

    private boolean verifyAvailableVolume(Long volume, Long sectionId){
        Section section = findOrThrowSection(sectionId);

        return section.getVolume().compareTo(volume) >= 0;
    }
}
