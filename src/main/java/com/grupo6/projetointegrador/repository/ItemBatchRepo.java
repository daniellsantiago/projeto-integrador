package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {
    ItemBatch findByProductId(Long productId);

    List<ItemBatch> findAllByProductId(Long productId);

    List<ItemBatch> findAllByProductIdOrderByIdAsc(Long productId);

    List<ItemBatch> findAllByProductIdOrderByProductQuantityAsc(Long productId);

    List<ItemBatch> findAllByProductIdOrderByDueDateAsc(Long productId);
}
