package com.grupo6.projetointegrador.service;

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

@Service
public class ProductService {

    private ProductRepo productRepo;

    private ItemBatchRepo itemBatchRepo;

    public ProductService(ProductRepo productRepo, ItemBatchRepo itemBatchRepo) {
        this.productRepo = productRepo;
        this.itemBatchRepo = itemBatchRepo;
    }

    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result =  productRepo.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

    public PageableResponse findProductsByCategory(Pageable pageable, Category category) {
        Page<Product> result =  productRepo.findProductsByCategory(pageable, category);
        return new PageableResponse().toResponse(result);
    }

    private List<ItemBatch> findByProductId(Long productId){
        return itemBatchRepo.findAllByProductId(productId);
    }

    private SectionDto createSectionDto(ItemBatch itemBatch){
        return new SectionDto(itemBatch.getInboundOrder().getSection().getId(),
                itemBatch.getInboundOrder().getWarehouse().getId());
    }

    public PageableResponse findProductsByOrder(Pageable pageable, String id, String order) {
        Page<Product> result = productRepo.findProductsByOrder(pageable, id, order);
        return new PageableResponse().toResponse(result);
    }
}
