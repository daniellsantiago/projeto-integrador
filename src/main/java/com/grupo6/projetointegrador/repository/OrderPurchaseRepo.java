package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderPurchaseRepo extends JpaRepository<OrderPurchase, Long> {
    Optional<OrderPurchase> findByIdAndStatus(Long id, StatusOrder statusOrder);
}

