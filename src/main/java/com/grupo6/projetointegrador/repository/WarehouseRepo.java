package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
}
