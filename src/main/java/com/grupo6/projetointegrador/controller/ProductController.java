package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.response.PageableResponse;
import com.grupo6.projetointegrador.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
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
    @GetMapping("/fresh-products/list")
    public ResponseEntity<ProductLocationDto> findProductById(@RequestParam(name = "id", required = true) Long id,
                                                              @RequestParam(name = "order", defaultValue = "V", required = true) String order){
        return ResponseEntity.ok(productService.findProductById(id, order));
    }

    @GetMapping("/fresh-products")
    public ResponseEntity<?> findAllFreshProducts(@RequestParam(value = "page", defaultValue = "0", required = true) int page) {
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findPageableFreshProducts(pageRequest);
        if(result.getContent().size() == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @GetMapping("/fresh-products/search")
    public ResponseEntity<?> findProductsCategory(
            @RequestParam(name = "category", defaultValue = "FF", required = true) String categoryCode,
            @RequestParam(value = "page", defaultValue = "0", required = true) int page) {

        Category category = Category.fromCode(categoryCode);
        PageRequest pageRequest = PageRequest.of(page, MAX_LENGTH_ITENS);
        PageableResponse result = this.productService.findProductsByCategory(pageRequest, category);
        if(result.getContent().size() == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }
}
