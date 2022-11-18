package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.dto.ProductWarehousesDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.response.PageableResponse;
import com.grupo6.projetointegrador.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    static final int MAX_LENGTH_ITENS = 5;
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Documentar aqui.
     * L = ordenado por lote
     * Q = ordenado por quantidade atual
     * V = ordenado por data de vencimento
     * @param id
     * @param order
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductLocationDto> findProductById(@PathVariable Long id,
                                                              @RequestParam(name = "order", defaultValue = "V", required = true) String order){
        return ResponseEntity.ok(productService.findProductById(id, order));
    }

    @GetMapping
    public ResponseEntity<?> findAllFreshProducts(@RequestParam(value = "page", defaultValue = "0", required = true) int page) {
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findPageableFreshProducts(pageRequest);
        if(result.getContent().size() == 0)
            throw new NotFoundException("Nenhum lote encontrado.");
        else
            return ResponseEntity.ok(result);
    }

    @GetMapping("/category-search")
    public ResponseEntity<?> findProductsCategory(
            @RequestParam(name = "category", defaultValue = "FF", required = true) String categoryCode,
            @RequestParam(value = "page", defaultValue = "0", required = true) int page) {

        Category category = Category.fromCode(categoryCode);
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findProductsByCategory(pageRequest, category);
        if(result.getContent().size() == 0)
            throw new NotFoundException("Produtos para essa categoria não encontrados.");
        else
            return ResponseEntity.ok(result);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<ProductWarehousesDto> findProductWarehouses(@PathVariable Long id) {
        ProductWarehousesDto response = productService.findProductWarehouse(id);
        if(response == null) {
            throw new NotFoundException("Nenhum armazém encontrado com esse produto.");
        }
        return ResponseEntity.ok(productService.findProductWarehouse(id));
    }
}
