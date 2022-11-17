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

    /**
     * This method creates an inbound order, validates the input, and returns a list of item batches.<p>
     * Also, check the {@link #validateInboundOrderCreation(Long, Long, Long, List)} method for more movement details.
     *
     * @param createInboundOrderDto The DTO that contains the data for the inbound order.
     * @return A list of ItemBatchDto objects.
     */
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
                .orElseThrow(() -> new NotFoundException("Ordem de entrada não encontrado."));

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

    /**
     * Method to verify if inbound order is valid to be created.<p>
     * Also, check the {@link #verifyWarehouseMatchWithOperator(Long, Long)} method for more movement details.<p>
     * Also, check the {@link #verifyIfSectionCanStoreItems(Long, Long, List)} method for more movement details.
     *
     * @param sectionId           The section where the items will be stored.
     * @param warehouseId         The warehouse where the inbound order is being created.
     * @param warehouseOperatorId The id of the user who is creating the order.
     * @param itemBatchDto        A list of items to be stored in the warehouse.
     */
    private void validateInboundOrderCreation(
            Long sectionId,
            Long warehouseId,
            Long warehouseOperatorId,
            List<CreateItemBatchDto> itemBatchDto
    ) {
        verifyWarehouseMatchWithOperator(warehouseOperatorId, warehouseId);
        verifyIfSectionCanStoreItems(sectionId, warehouseId, itemBatchDto);
    }

    /**
     * Method to find a warehouse by id or throw a not found exception.
     *
     * @param warehouseId The ID of the warehouse.
     * @return A warehouse object or {@link NotFoundException} - if none found.<p>
     */
    private Warehouse findWarehouseOrThrowNotFound(Long warehouseId) {
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
     * Method to verify if the warehouse operator id does not match the warehouse id,
     * throws {@link BusinessRuleException} - if id not equals.<p>
     * Also, check the {@link #findWarehouseOrThrowNotFound(Long)} method for more movement details.
     *
     * @param warehouseOperatorId The id of the warehouse operator that is trying to access the warehouse.
     * @param warehouseId         The warehouse id that the operator is trying to access.
     */
    private void verifyWarehouseMatchWithOperator(Long warehouseOperatorId, Long warehouseId) {
        if (!findWarehouseOrThrowNotFound(warehouseId).getWarehouseOperator().getId().equals(warehouseOperatorId)) {
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }

    /**
     * Method to verify if a section is valid and available to store items.<p>
     * Also, check the {@link #verifyWarehouseMatchSection(Long, Long)} method for more movement details.<p>
     * Also, check the {@link #verifySectionVolumeAvailability(List, Long)} method for more movement details.<p>
     * Also, check the {@link #verifyIfStorageTypeMatchSection(List, Long)} method for more movement details.<p>
     * Also, check the {@link #verifyIfProductTypeMatchStorageType(List)} method for more movement details.
     *
     * @param sectionId    The section where the items will be stored.
     * @param warehouseId  The warehouse where the section is located.
     * @param itemBatchDto a list of items to be stored in the warehouse.
     */
    private void verifyIfSectionCanStoreItems(Long sectionId, Long warehouseId, List<CreateItemBatchDto> itemBatchDto) {
        verifyWarehouseMatchSection(sectionId, warehouseId);
        verifySectionVolumeAvailability(itemBatchDto, sectionId);
        verifyIfStorageTypeMatchSection(itemBatchDto, sectionId);
        verifyIfProductTypeMatchStorageType(itemBatchDto);
    }

    /**
     * Method to find a section by id or throw a not found exception.
     *
     * @param sectionId The id of the section.
     * @return A section object or {@link NotFoundException} if none found.<p>
     */
    private Section findSectionOrThrowNotFound(Long sectionId) {
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }

    /**
     * Method to verify if the sum of the volumes of the items in the batch is greater than the volume of the section,
     * throws {@link BusinessRuleException} - if the available volume of the session is less than {@code volumeTotal}.<p>
     * Also, check the {@link #findSectionOrThrowNotFound(Long)} method for more movement details.
     *
     * @param itemBatchDto List of items to be created
     * @param sectionId    The id of the section where the items will be stored.
     */
    private void verifySectionVolumeAvailability(List<CreateItemBatchDto> itemBatchDto, Long sectionId) {
        Long volumeTotal = itemBatchDto.stream().map(CreateItemBatchDto::getVolume).reduce(0L, Long::sum);
        Section section = findSectionOrThrowNotFound(sectionId);
        if (section.getVolume().compareTo(volumeTotal) < 0) {
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
    }

    /**
     * Method to verify if the section does not belong to the warehouse, throw an exception,
     * throws {@link BusinessRuleException} - if id not equals.<p>
     * Also, check the {@link #findSectionOrThrowNotFound(Long)} method for more movement details.
     *
     * @param sectionId   The id of the section to be verified.
     * @param warehouseId The id of the warehouse that the section belongs to.
     */
    private void verifyWarehouseMatchSection(Long sectionId, Long warehouseId) {
        if (!findSectionOrThrowNotFound(sectionId).getWarehouse().getId().equals(warehouseId)) {
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    /**
     * Method to verify if the storage type of the batch is compatible with the section,
     * throws {@link BusinessRuleException} - if storage type not equals.<p>
     * Also, check the {@link #findSectionOrThrowNotFound(Long)} method for more movement details.
     *
     * @param itemBatchDto List of items to be created
     * @param sectionId    The id of the section where the item will be stored.
     */
    private void verifyIfStorageTypeMatchSection(List<CreateItemBatchDto> itemBatchDto, Long sectionId) {
        Section section = findSectionOrThrowNotFound(sectionId);
        itemBatchDto.forEach((batch) -> {
            if (!section.getStorageType().getName().equals(batch.getStorageType().getName())) {
                throw new BusinessRuleException("O tipo de armazenamento do lote não é compatível com a seção.");
            }
        });
    }

    /**
     * Method to verify if the storage type of the batch is compatible with the product,
     * throws {@link BusinessRuleException} - if id not find.
     *
     * @param itemBatchDto List of CreateItemBatchDto objects
     */
    private void verifyIfProductTypeMatchStorageType(List<CreateItemBatchDto> itemBatchDto) {
        itemBatchDto.forEach((batch) -> {
            Product product = productRepo.findById(batch.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
            if (!product.getCategory().getName().equals(batch.getStorageType().getName())) {
                throw new BusinessRuleException("O tipo de armazenamento do lote não é compatível com o produto.");
            }
        });
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
}
