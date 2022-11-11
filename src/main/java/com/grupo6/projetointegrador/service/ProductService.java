package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.Product;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result =  productRepo.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

}
