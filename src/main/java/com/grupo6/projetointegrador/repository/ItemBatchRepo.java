package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {
}