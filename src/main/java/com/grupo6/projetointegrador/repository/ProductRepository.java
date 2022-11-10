package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p")
    Page<Product> findPageableProducts(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.category = ?1")
    Page<Product> findProductsByCategory(Pageable pageable, Category category);

    //inner join product.id ->
    @Query(value = "SELECT p FROM Product p INNER JOIN ON p.seller.id = ?1 ORDER BY p.price " + "?2")
    Page<Product> findProductsByOrder(Pageable pageable, String id, String order);
}
