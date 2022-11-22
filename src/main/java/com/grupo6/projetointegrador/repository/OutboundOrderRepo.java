package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.OutboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundOrderRepo extends JpaRepository<OutboundOrder, Long> {
}

