package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.WarehouseDto;
import com.grupo6.projetointegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p")
    Page<Product> findPageableProducts(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.category = ?1")
    Page<Product> findProductsByCategory(Pageable pageable, Category category);

    Page<Product> findPageableProductsByCategory(Pageable pageable, String category);

    @Query(value = "SELECT t3.warehouse_id AS warehouse, SUM(t2.product_quantity) as quantity FROM `product` t1 " +
            "RIGHT JOIN item_batch t2 ON t2.product_id = t1.id " +
            "LEFT JOIN inbound_order t3 ON t3.id = t2.inbound_order_id " +
            "WHERE t1.id = (?1) AND t2.product_quantity > 0 GROUP BY t3.warehouse_id", nativeQuery = true)
    List<WarehouseDto> findWarehousesByProduct(Long id);
}
