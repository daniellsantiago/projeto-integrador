package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductLocationDto findProductById(Long productId, String order);
    PageableResponse findProductsByCategory(Pageable pageable, Category category);
    PageableResponse findPageableFreshProducts(Pageable pageable);
}
