package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ProductWarehousesDto;
import com.grupo6.projetointegrador.dto.WarehouseDto;
import com.grupo6.projetointegrador.model.Product;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public ProductWarehousesDto findProductWarehouse(Long id) {
        List<WarehouseDto> warehouses = productRepo.findWarehousesByProduct(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum armazém com esse produto encontrado."));
        return new ProductWarehousesDto(id, warehouses);
    }
}
