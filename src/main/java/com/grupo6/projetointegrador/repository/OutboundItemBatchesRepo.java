package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.OutboundItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundItemBatchesRepo extends JpaRepository<OutboundItemBatch, Long> {
}
