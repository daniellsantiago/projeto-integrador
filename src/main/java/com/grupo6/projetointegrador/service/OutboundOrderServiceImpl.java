package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOutboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateOutboundItemBatchDto;
import com.grupo6.projetointegrador.dto.OutboundItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateOutboundItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
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
    private final WarehouseRepo warehouseRepo;

    private final WarehouseOperatorRepo warehouseOperatorRepo;

    private final SectionRepo sectionRepo;

    private final ProductRepo productRepo;

    public OutboundOrderServiceImpl(
            OutboundOrderRepo outboundOrderRepo,
            WarehouseRepo warehouseRepo,
            WarehouseOperatorRepo warehouseOperatorRepo,
            ProductRepo productRepo,
            SectionRepo sectionRepo
    ) {
        this.outboundOrderRepo = outboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.productRepo = productRepo;
        this.sectionRepo = sectionRepo;
    }

    /**
     * This method receives a DTO with a list of items and ids.
     * The main goal is to store those items on the provided section.<p>
     * Also, check the {@link #validateOutboundOrderCreation(List, Warehouse, WarehouseOperator, Section, List)} method for validation details.<p>
     * @param createOutboundOrderDto This is the object that will be sent by the frontend.
     * @return A List<OutboundItemBatchDto> object with the stored items.
     */
    @Override
    @Transactional
    public List<OutboundItemBatchDto> createOutboundOrder(CreateOutboundOrderDto createOutboundOrderDto) {
        Warehouse warehouse = findWarehouseOrThrowNotFound(createOutboundOrderDto.getWarehouseId());
        Section section = findSectionOrThrowNotFound(createOutboundOrderDto.getSectionId());
        WarehouseOperator warehouseOperator = findWarehouseOperatorOrThrowNotFound(createOutboundOrderDto.getWarehouseOperatorId());
        List<Product> products = createOutboundOrderDto.getOutboundItemBatches().stream()
                .map((batchDto) -> findProductOrThrowNotFound(batchDto.getProductId()))
                .collect(Collectors.toList());

        validateOutboundOrderCreation(
                createOutboundOrderDto.getOutboundItemBatches(),
                warehouse,
                warehouseOperator,
                section,
                products
        );

        OutboundOrder createdOutboundOrder = new OutboundOrder();
        List<OutboundItemBatch> itemBatches = createOutboundOrderDto.getOutboundItemBatches().stream().map((batchDto) -> {
            Product foundProduct = products.stream()
                    .filter(product -> product.getId().equals(batchDto.getProductId()))
                    .findFirst().get();
            return batchDto.toOutboundItemBatch(createdOutboundOrder, foundProduct);
        }).collect(Collectors.toList());

        createdOutboundOrder.setOrderDate(LocalDate.now());
        createdOutboundOrder.setSection(section);
        createdOutboundOrder.setWarehouse(warehouse);
        createdOutboundOrder.setWarehouseOperator(warehouseOperator);
        createdOutboundOrder.setOutboundItemBatches(itemBatches);

        OutboundOrder savedOutboundOrder = outboundOrderRepo.save(createdOutboundOrder);

        return savedOutboundOrder.getOutboundItemBatches().stream().map(OutboundItemBatchDto::fromOutboundItemBatch).collect(Collectors.toList());
    }

    /**
     * Receives the OutboundOrder Id and a list o OutboundItemBatch.
     * It'll update the provided OutboundOrder OutboundItemBatches<p>
     * Also, check the {@link #validateOutboundOrderUpdate(List, OutboundOrder, List)} method for validation details.<p>
     * @param outboundOrderId This is the OutboundOrder ID.
     * @param updateOutboundItemBatchDtos This is the object that will be sent by the frontend.
     * @return A List<OutboundItemBatchDto> object with the stored items.
     */
    @Override
    @Transactional
    public List<OutboundItemBatchDto> updateOutboundItemBatch(Long outboundOrderId, List<UpdateOutboundItemBatchDto> updateOutboundItemBatchDtos) {
        OutboundOrder outboundOrder = outboundOrderRepo.findById(outboundOrderId)
                .orElseThrow(() -> new NotFoundException("Ordem de entrada não encontrado."));
        List<Product> products = updateOutboundItemBatchDtos.stream()
                .map(updateDto -> findProductOrThrowNotFound(updateDto.getProductId()))
                        .collect(Collectors.toList());
        validateOutboundOrderUpdate(updateOutboundItemBatchDtos, outboundOrder, products);

        List<OutboundItemBatch> updatedOutboundItemBatches = updateOutboundItemBatchDtos.stream()
                .map(itemBatchDto -> itemBatchDto.toOutboundItemBatch(
                        outboundOrder,
                        products.stream()
                                .filter(product -> itemBatchDto.getProductId().equals(product.getId()))
                                .findFirst().get()
                ))
                .collect(Collectors.toList());

        outboundOrder.setOutboundItemBatches(updatedOutboundItemBatches);
        outboundOrderRepo.save(outboundOrder);

        return updatedOutboundItemBatches.stream()
                .map(OutboundItemBatchDto::fromOutboundItemBatch)
                .collect(Collectors.toList());
    }

    /**
     * Method to find a warehouse by id or throw a not found exception.
     *
     * @param warehouseId The ID of the warehouse.
     * @return A warehouse object or {@link NotFoundException} - if none found.<p>
     */
    private Warehouse findWarehouseOrThrowNotFound(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    /**
     * Method to find a warehouse operator by id or throw a not found exception.
     *
     * @param warehouseOperatorId The ID of the warehouse operator to be updated.
     * @return A WarehouseOperator object or {@link NotFoundException} - if none found.<p>
     */
    private WarehouseOperator findWarehouseOperatorOrThrowNotFound(Long warehouseOperatorId) {
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    /**
     * Method to find a Section giving It id.
     *
     * @param sectionId The id of the section.
     * @return A section with the given id or {@link NotFoundException} - if none found.
     */
    private Section findSectionOrThrowNotFound(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }
    /**
     * Method to find a product giving id.
     *
     * @param productId The id of the product.
     * @return A product with the given id or {@link NotFoundException} - if none found.
     */
    private Product findProductOrThrowNotFound(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }

    /**
     * Validates if an OutboundOrder can be created.
     * @param itemBatchDtos List of items to be created.
     * @param warehouse Warehouse where It'll be stored.
     * @param warehouseOperator Who is going to store It.
     * @param section Section where It'll be stored.
     * @param products Products to be stored.
     */
    private void validateOutboundOrderCreation(
            List<CreateOutboundItemBatchDto> itemBatchDtos,
            Warehouse warehouse,
            WarehouseOperator warehouseOperator,
            Section section,
            List<Product> products
    ) {
        Long volumeToBeStored = itemBatchDtos.stream().map(CreateOutboundItemBatchDto::getVolume)
                .reduce(0L, Long::sum);

        verifyWarehouseMatchWithOperator(warehouse, warehouseOperator);
        verifyWarehouseMatchSection(section, warehouse);
        verifyIfProductsCategoryDifferFromSection(products, section);
        verifyIfSectionCanStoreItems(section, volumeToBeStored);
    }

    /**
     * Validates if the OutboundOrder OutboundItemBatches can be updated.
     * @param itemBatchDtos List of OutboundOrder items to be updated.
     * @param outboundOrder OutboundOrder to be updated.
     * @param products Products to be updated.
     */
    private void validateOutboundOrderUpdate(
            List<UpdateOutboundItemBatchDto> itemBatchDtos,
            OutboundOrder outboundOrder,
            List<Product> products
    ) {
        Long volumeToBeStored = itemBatchDtos.stream().map(UpdateOutboundItemBatchDto::getVolume)
                .reduce(0L, Long::sum);
        Section section = outboundOrder.getSection();
        verifyIfProductsCategoryDifferFromSection(products, section);
        verifyIfSectionCanStoreItems(section, volumeToBeStored);
    }

    /**
     * Verify if the section has enough volume to store the items.
     * @param section The id of the section.
     * @param volumeToBeStored The quantity of volume to be used.
     * @throws BusinessRuleException if volume is not valid.
     */
    private void verifyIfSectionCanStoreItems(Section section, Long volumeToBeStored) {
        if (section.getVolume().compareTo(volumeToBeStored) < 0) {
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
    }

    /**
     * Verify if the Section and Warehouse are related.
     * @param section The section.
     * @param warehouse The warehouse.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyWarehouseMatchSection(Section section, Warehouse warehouse) {
        if(!section.getWarehouse().getId().equals(warehouse.getId())){
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    /**
     * Verify if the provided Products categories are equal to the given Section.
     * @param products Product list.
     * @param section Section to be compared.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyIfProductsCategoryDifferFromSection(List<Product> products, Section section) {
        boolean validStorageType = products.stream()
                .map(Product::getCategory)
                .allMatch(storageType -> section.getCategory().getName().equals(storageType.getName()));
        if(!validStorageType) {
            throw new BusinessRuleException("A categoria do Produto não é compatível com a seção.");
        }
    }

    /**
     * Verify if the Warehouse and Warehouse Operator are related.
     * @param warehouseOperator The operator.
     * @param warehouse The warehouse.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyWarehouseMatchWithOperator(Warehouse warehouse, WarehouseOperator warehouseOperator) {
        if(!warehouse.getWarehouseOperator().getId().equals(warehouseOperator.getId())){
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }
}
