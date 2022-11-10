package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.*;
import com.grupo6.projetointegrador.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderServiceImpl implements InboundOrderService{

    private InboundOrderRepo inboundOrderRepo;
    private WarehouseRepo warehouseRepo;
    private WarehouseOperatorRepo warehouseOperatorRepo;
    private SectionRepo sectionRepo;

    private ProductRepo productRepo;

    public InboundOrderServiceImpl(InboundOrderRepo inboundOrderRepo, WarehouseRepo warehouseRepo, WarehouseOperatorRepo warehouseOperatorRepo, SectionRepo sectionRepo, ProductRepo productRepo) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.sectionRepo = sectionRepo;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {

        verifyEverythingIsOk(createInboundOrderDto.getSectionId(),
                createInboundOrderDto.getWarehouseId(),
                createInboundOrderDto.getWarehouseOperatorId(),
                createInboundOrderDto.getItemBatches());

        InboundOrder inboundOrderToPersist = new InboundOrder();
        WarehouseOperator warehouseOperator = findOrThrowWarehouseOperator(createInboundOrderDto.getWarehouseOperatorId());
        Warehouse warehouse = findOrThrowWarehouse(createInboundOrderDto.getWarehouseId());
        Section section = findOrThrowSection(createInboundOrderDto.getSectionId());
        List<ItemBatch> itemBatches = createInboundOrderDto.getItemBatches().stream().map((batchDto) -> {
            Product product = productRepo.findById(batchDto.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
            return batchDto.toItemBatch(inboundOrderToPersist, product);
        }).collect(Collectors.toList());

        inboundOrderToPersist.setOrderDate(LocalDate.now());
        inboundOrderToPersist.setSection(section);
        inboundOrderToPersist.setWarehouse(warehouse);
        inboundOrderToPersist.setWarehouseOperator(warehouseOperator);
        inboundOrderToPersist.setItemBatches(itemBatches);

        InboundOrder savedInboundOrder = inboundOrderRepo.save(inboundOrderToPersist);

        return savedInboundOrder.getItemBatches().stream().map(ItemBatchDto::fromItemBatch).collect(Collectors.toList());
    }

    private void verifyEverythingIsOk(Long sectionId, Long warehouseId, Long warehouseOperatorId, List<CreateItemBatchDto> itemBatchDto){
        verifyWarehouseMatchWithOperator(warehouseOperatorId, warehouseId);
        verifySectionIsOk(sectionId, warehouseId, itemBatchDto);
    }

    private Warehouse findOrThrowWarehouse(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator findOrThrowWarehouseOperator(Long warehouseOperatorId){
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    private boolean verifyWarehouseMatchWithOperator(Long warehouseOperatorId, Long warehouseId){
        Warehouse warehouse = findOrThrowWarehouse(warehouseId);
        if(warehouse.getWarehouseOperator().getId().equals(warehouseOperatorId)){
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

    private void verifyAvailableVolume(List<CreateItemBatchDto> itemBatchDto, Long sectionId) {
        Long volumeTotal = itemBatchDto.stream().map(CreateItemBatchDto::getVolume).reduce(0L, Long::sum);
        Section section = findOrThrowSection(sectionId);
        if (section.getVolume().compareTo(volumeTotal) < 0){
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
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
