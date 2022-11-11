package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, Long> {
}
