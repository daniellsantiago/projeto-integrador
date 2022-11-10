package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundOrderRepo extends JpaRepository<InboundOrder, Long> {
}
