package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    private final ItemBatchRepo itemBatchRepo;

    public ProductServiceImpl(ProductRepo productRepo, ItemBatchRepo itemBatchRepo) {
        this.productRepo = productRepo;
        this.itemBatchRepo = itemBatchRepo;
    }

    @Override
    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result =  productRepo.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

    @Override
    public PageableResponse findProductsByCategory(Pageable pageable, Category category) {
        Page<Product> result =  productRepo.findProductsByCategory(pageable, category);
        return new PageableResponse().toResponse(result);
    }

    @Override
    public ProductLocationDto findProductById(Long productId, String order){
        verifyProductExists(productId);
        List<ItemBatch> itemBatchList = findItemBatchByProductId(productId, order);
        List<SectionDto> sectionDto = createSectionDto(itemBatchList);
        List<ItemBatchLocationDto> itemBatchLocationDto = itemBatchList.stream().map(ItemBatchLocationDto::fromItemBatch).collect(Collectors.toList());
        return new ProductLocationDto(sectionDto, productId, itemBatchLocationDto);
    }

    private void verifyProductExists(Long productId) {
        productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto com esse id não cadastrado."));
    }

    private List<ItemBatch> findItemBatchByProductId(Long productId, String order){
        List<ItemBatch> itemBatchList;
        switch (order){
            case "L":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByIdAsc(productId);
                break;
            case "Q":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByProductQuantityAsc(productId);
                break;
            case "V":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByDueDateAsc(productId);
                break;
            default:
                itemBatchList = itemBatchRepo.findAllByProductId(productId);
                break;
        }
        if (itemBatchList.isEmpty()){
            throw new NotFoundException("Lotes para esse produto não encontrados.");
        }
        return itemBatchList;
    }

    private List<SectionDto> createSectionDto(List<ItemBatch> itemBatch){
        List<SectionDto> sectionDtoList = new ArrayList<>();

        List<Long> inboundOrderCovered = new ArrayList<>();

        itemBatch.forEach((batch) -> {
            Long batchInboundOrderId = batch.getInboundOrder().getId();

            if (!inboundOrderCovered.contains(batchInboundOrderId)){
                SectionDto sectionDto = new SectionDto(batch.getInboundOrder().getSection().getId(),
                        batch.getInboundOrder().getWarehouse().getId());
                sectionDtoList.add(sectionDto);
                inboundOrderCovered.add(batchInboundOrderId);
            }
        });

        return sectionDtoList;
    }

    @Override
    public ProductWarehousesDto findProductWarehouse(Long id) {
        List<WarehouseDto> warehouses = productRepo.findWarehousesByProduct(id);
        if (warehouses.isEmpty()) {
            return null;
        }
        return new ProductWarehousesDto(id, warehouses);
    }
}
