package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ProductRepository;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result =  productRepository.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

    public PageableResponse findProductsByCategory(Pageable pageable, Category category) {
        Page<Product> result =  productRepository.findProductsByCategory(pageable, category);
        return new PageableResponse().toResponse(result);
    }
}
