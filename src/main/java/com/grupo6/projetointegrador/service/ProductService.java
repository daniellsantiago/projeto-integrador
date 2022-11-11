package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ItemBatchLocationDto;
import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.dto.SectionDto;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepo productRepo;

    private ItemBatchRepo itemBatchRepo;

    public ProductService(ProductRepo productRepo, ItemBatchRepo itemBatchRepo) {
        this.productRepo = productRepo;
        this.itemBatchRepo = itemBatchRepo;
    }


    public ProductLocationDto findProductById(Long productId){
        List<ItemBatch> itemBatchList = findItemBatchByProductId(productId);
        SectionDto sectionDto = createSectionDto(itemBatchList.stream().findFirst().orElseThrow());
        List<ItemBatchLocationDto> itemBatchLocationDto = itemBatchList.stream().map(ItemBatchLocationDto::fromItemBatch).collect(Collectors.toList());
        return new ProductLocationDto(sectionDto, productId, itemBatchLocationDto);
    }

    private List<ItemBatch> findItemBatchByProductId(Long productId){
        return itemBatchRepo.findAllByProductId(productId);
    }

    private SectionDto createSectionDto(ItemBatch itemBatch){
        return new SectionDto(itemBatch.getInboundOrder().getSection().getId(),
                itemBatch.getInboundOrder().getWarehouse().getId());
    }
/*
    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result =  productRepo.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

    public PageableResponse findProductsByCategory(Pageable pageable, Category category) {
        Page<Product> result =  productRepo.findProductsByCategory(pageable, category);
        return new PageableResponse().toResponse(result);
    }


    public PageableResponse findProductsByOrder(Pageable pageable, String id, String order) {
        Page<Product> result = productRepo.findProductsByOrder(pageable, id, order);
        return new PageableResponse().toResponse(result);
    }*/
}
