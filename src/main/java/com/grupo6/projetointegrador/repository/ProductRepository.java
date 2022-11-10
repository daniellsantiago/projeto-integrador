package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p")
    Page<Product> findPageableProducts(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.category = ?1")
    Page<Product> findPageableProductsByCategory(Pageable pageable, String category);

}
