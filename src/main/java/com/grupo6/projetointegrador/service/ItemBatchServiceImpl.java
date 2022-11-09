package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.ItemBatch;
import com.grupo6.projetointegrador.model.Product;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import org.springframework.stereotype.Service;

@Service
public class ItemBatchServiceImpl implements ItemBatchService{

    private ItemBatchRepo itemBatchRepo;
    private ProductRepo productRepo;

    public ItemBatchServiceImpl(ItemBatchRepo itemBatchRepo, ProductRepo productRepo) {
        this.itemBatchRepo = itemBatchRepo;
        this.productRepo = productRepo;
    }

    @Override
    public ItemBatch createItemBatch(CreateItemBatchDto itemBatchDto) {
        Product product = productRepo.findById(itemBatchDto.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        return null;
    }
}
