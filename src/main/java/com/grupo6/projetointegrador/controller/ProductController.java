package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.ProductWarehousesDto;
import com.grupo6.projetointegrador.response.PageableResponse;
import com.grupo6.projetointegrador.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    static final int MAX_LENGTH_ITENS = 10;
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/fresh-products")
    public ResponseEntity<?> findAllFreshProducts(@RequestParam(value = "page",defaultValue = "0", required = true) int page) {
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findPageableFreshProducts(pageRequest);
        if(result.getContent().size() == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

  /*  @GetMapping("/fresh-products")
    public ResponseEntity<?> findAllFreshProducts(@RequestParam(value = "category",defaultValue = "0", required = true) int page) {
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findPageableFreshProducts(pageRequest);
        if(result.getContent().size() == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }*/

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<ProductWarehousesDto> findProductWarehouses(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductWarehouse(id));
    }
}
