package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderServiceImpl implements InboundOrderService {

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
        Warehouse warehouse = findWarehouseOrThrowNotFound(createInboundOrderDto.getWarehouseId());
        Section section = findSectionOrThrowNotFound(createInboundOrderDto.getSectionId());
        WarehouseOperator warehouseOperator = findWarehouseOperatorOrThrowNotFound(createInboundOrderDto.getWarehouseOperatorId());
        List<Product> products = createInboundOrderDto.getItemBatches().stream()
                .map((batchDto) -> findProductOrThrowNotFound(batchDto.getProductId()))
                .collect(Collectors.toList());

        validateInboundOrderCreation(
                createInboundOrderDto.getItemBatches(),
                warehouse,
                warehouseOperator,
                section,
                products
        );

        InboundOrder createdInboundOrder = new InboundOrder();
        List<ItemBatch> itemBatches = createInboundOrderDto.getItemBatches().stream().map((batchDto) -> {
            Product foundProduct = products.stream()
                    .filter(product -> product.getId().equals(batchDto.getProductId()))
                    .findFirst().get();
            return batchDto.toItemBatch(createdInboundOrder, foundProduct);
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

    private Warehouse findWarehouseOrThrowNotFound(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    private WarehouseOperator findWarehouseOperatorOrThrowNotFound(Long warehouseOperatorId){
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    private Section findSectionOrThrowNotFound(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }

    private Product findProductOrThrowNotFound(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }

    private void validateInboundOrderCreation(
            List<CreateItemBatchDto> itemBatchDtos,
            Warehouse warehouse,
            WarehouseOperator warehouseOperator,
            Section section,
            List<Product> products
    ) {
        Long volumeToBeStored = itemBatchDtos.stream().map(CreateItemBatchDto::getVolume)
                .reduce(0L, Long::sum);

        verifyWarehouseMatchWithOperator(warehouse, warehouseOperator);
        verifyWarehouseMatchSection(section, warehouse);
        verifyIfProductsCategoryDifferFromSection(products, section);
        verifyIfSectionCanStoreItems(section, volumeToBeStored);
    }

    private void verifyIfSectionCanStoreItems(Section section, Long volumeToBeStored) {
        if (section.getVolume().compareTo(volumeToBeStored) < 0) {
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
    }

    private void verifyWarehouseMatchSection(Section section, Warehouse warehouse) {
        if(!section.getWarehouse().getId().equals(warehouse.getId())){
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    private void verifyIfProductsCategoryDifferFromSection(List<Product> products, Section section) {
        boolean validStorageType = products.stream()
                .map(Product::getCategory)
                .allMatch(storageType -> section.getCategory().getName().equals(storageType.getName()));
        if(!validStorageType) {
            throw new BusinessRuleException("A categoria do Produto não é compatível com a seção.");
        }
    }

    private void verifyWarehouseMatchWithOperator(Warehouse warehouse, WarehouseOperator warehouseOperator) {
        if(!warehouse.getWarehouseOperator().getId().equals(warehouseOperator.getId())){
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }
}
