package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.*;
import com.grupo6.projetointegrador.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderServiceImpl implements InboundOrderService{

    private final InboundOrderRepo inboundOrderRepo;
    private final WarehouseRepo warehouseRepo;

    private final WarehouseOperatorRepo warehouseOperatorRepo;

    private final SectionRepo sectionRepo;

    private final ProductRepo productRepo;

    public InboundOrderServiceImpl(
            InboundOrderRepo inboundOrderRepo,
            WarehouseRepo warehouseRepo,
            WarehouseOperatorRepo warehouseOperatorRepo,
            ProductRepo productRepo,
            SectionRepo sectionRepo
    ) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.productRepo = productRepo;
        this.sectionRepo = sectionRepo;
    }

    @Override
    @Transactional
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {

        validateInboundOrderCreation(
                createInboundOrderDto.getSectionId(),
                createInboundOrderDto.getWarehouseId(),
                createInboundOrderDto.getWarehouseOperatorId(),
                createInboundOrderDto.getItemBatches()
        );

        InboundOrder createdInboundOrder = new InboundOrder();
        WarehouseOperator warehouseOperator = findWarehouseOperatorOrThrowNotFound(createInboundOrderDto.getWarehouseOperatorId());
        Warehouse warehouse = findWarehouseOrThrowNotFound(createInboundOrderDto.getWarehouseId());
        Section section = findSectionOrThrowNotFound(createInboundOrderDto.getSectionId());
        List<ItemBatch> itemBatches = createInboundOrderDto.getItemBatches().stream().map((batchDto) -> {
            Product product = findProductOrThrowNotFound(batchDto.getProductId());
            return batchDto.toItemBatch(createdInboundOrder, product);
        }).collect(Collectors.toList());

        createdInboundOrder.setOrderDate(LocalDate.now());
        createdInboundOrder.setSection(section);
        createdInboundOrder.setWarehouse(warehouse);
        createdInboundOrder.setWarehouseOperator(warehouseOperator);
        createdInboundOrder.setItemBatches(itemBatches);

        InboundOrder savedInboundOrder = inboundOrderRepo.save(createdInboundOrder);

        return savedInboundOrder.getItemBatches().stream().map(ItemBatchDto::fromItemBatch).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemBatchDto> updateItemBatch(Long inboundOrderId, List<UpdateItemBatchDto> updateItemBatchDtos) {
        InboundOrder inboundOrder = inboundOrderRepo.findById(inboundOrderId)
                .orElseThrow(() -> new NotFoundException("InboundOrder não encontrado"));

        List<ItemBatch> updatedItemBatches = updateItemBatchDtos.stream()
                .map(itemBatchDto -> itemBatchDto.toItemBatch(
                        inboundOrder,
                        findProductOrThrowNotFound(itemBatchDto.getProductId())
                ))
                .collect(Collectors.toList());

        inboundOrder.setItemBatches(updatedItemBatches);
        inboundOrderRepo.save(inboundOrder);

        return updatedItemBatches.stream()
                .map(ItemBatchDto::fromItemBatch)
                .collect(Collectors.toList());
    }

    private void validateInboundOrderCreation(
            Long sectionId,
            Long warehouseId,
            Long warehouseOperatorId,
            List<CreateItemBatchDto> itemBatchDto
    ){
        verifyWarehouseMatchWithOperator(warehouseOperatorId, warehouseId);
        verifyIfSectionCanStoreItems(sectionId, warehouseId, itemBatchDto);
    }

    private Warehouse findWarehouseOrThrowNotFound(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator findWarehouseOperatorOrThrowNotFound(Long warehouseOperatorId){
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    private void verifyWarehouseMatchWithOperator(Long warehouseOperatorId, Long warehouseId){
        if(!findWarehouseOrThrowNotFound(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)){
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }

    private void verifyIfSectionCanStoreItems(Long sectionId, Long warehouseId, List<CreateItemBatchDto> itemBatchDto){
        verifyWarehouseMatchSection(sectionId, warehouseId);
        verifySectionVolumeAvailability(itemBatchDto, sectionId);
        verifyIfProductTypeMatchSection(itemBatchDto, sectionId);
    }

    private Section findSectionOrThrowNotFound(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }

    private void verifySectionVolumeAvailability(List<CreateItemBatchDto> itemBatchDto, Long sectionId) {
        Long volumeTotal = itemBatchDto.stream().map(CreateItemBatchDto::getVolume).reduce(0L, Long::sum);
        Section section = findSectionOrThrowNotFound(sectionId);
        if (section.getVolume().compareTo(volumeTotal) < 0){
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
    }

    private void verifyWarehouseMatchSection(Long sectionId, Long warehouseId){
        if(!findSectionOrThrowNotFound(sectionId).getWarehouse().getId().equals(warehouseId)){
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    private void verifyIfProductTypeMatchSection(List<CreateItemBatchDto> itemBatchDto, Long sectionId){
        Section section = findSectionOrThrowNotFound(sectionId);
        itemBatchDto.forEach((batch) -> {
            if(!section.getStorageType().equals(batch.getStorageType())){
                throw new BusinessRuleException("O tipo de armazenamento do produto não é compatível com a seção");
            }
        });
    }

    private Product findProductOrThrowNotFound(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }
}
