package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.InboundOrder;
import com.grupo6.projetointegrador.model.Section;
import com.grupo6.projetointegrador.model.Warehouse;
import com.grupo6.projetointegrador.model.WarehouseOperator;
import com.grupo6.projetointegrador.repository.InboundOrderRepo;
import com.grupo6.projetointegrador.repository.SectionRepo;
import com.grupo6.projetointegrador.repository.WarehouseOperatorRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

        if (verifyEverithingIsOk(createInboundOrderDto.getSectionId(),
                createInboundOrderDto.getWarehouseId(),
                createInboundOrderDto.getWarehouseOperatorId(),
                createInboundOrderDto.getCreateItemBatchDtos())){

            InboundOrder inboundOrderToPersist = new InboundOrder();
            WarehouseOperator warehouseOperator = findOrThrowWarehouseOperator(createInboundOrderDto.getWarehouseOperatorId());
            Warehouse warehouse = findOrThrowWarehouse(createInboundOrderDto.getWarehouseId());
            Section section = findOrThrowSection(createInboundOrderDto.getSectionId());


            inboundOrderToPersist.setOrderDate(LocalDate.now());
            inboundOrderToPersist.setSection(section);
            inboundOrderToPersist.setWarehouse(warehouse);
            inboundOrderToPersist.setWarehouseOperator(warehouseOperator);

        }


        return null;
    }

    private boolean verifyEverithingIsOk(Long sectionId, Long warehouseId, Long warehouseOperatorId, List<CreateItemBatchDto> itemBatchDto){
        verifyWarehouseMatchWithOperator(warehouseOperatorId, warehouseId);
        verifySectionIsOk(sectionId, warehouseId, itemBatchDto);
        return true;
    }

    private Warehouse findOrThrowWarehouse(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator findOrThrowWarehouseOperator(Long warehouseOperatorId){
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    private boolean verifyWarehouseMatchWithOperator(Long warehouseOperatorId, Long warehouseId){
        if(findOrThrowWarehouse(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)){
            return true;
        } else {
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }

    // Funções de verificação Section
    private void verifySectionIsOk(Long sectionId, Long warehouseId, List<CreateItemBatchDto> itemBatchDto){
        verifyWarehouseMatchSection(sectionId, warehouseId);
        verifyAvailableVolume(itemBatchDto, sectionId);
        matchStorageType(itemBatchDto, sectionId);
    }

    private Section findOrThrowSection(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }

    private boolean verifyAvailableVolume(List<CreateItemBatchDto> itemBatchDto, Long sectionId) {
        Long volumeTotal = itemBatchDto.stream().map(CreateItemBatchDto::getVolume).reduce(0L, Long::sum);
        Section section = findOrThrowSection(sectionId);
        if (section.getVolume().compareTo(volumeTotal) < 0){
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        } else return true;
    }

    private Section verifyWarehouseMatchSection(Long sectionId, Long warehouseId){
        Section section = findOrThrowSection(sectionId);
        if(section.getWarehouse().getId().equals(warehouseId)){
            return section;
        } else {
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    private void matchStorageType(List<CreateItemBatchDto> itemBatchDto, Long sectionId){
        Section section = findOrThrowSection(sectionId);
        itemBatchDto.forEach((batch) -> {
            if(!section.getStorageType().equals(batch.getStorageType())){
                throw new BusinessRuleException("O tipo de armazenamento do produto não é compatível com a seção");
            }
        });
    }
}
