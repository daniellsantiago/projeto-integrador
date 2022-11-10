package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
