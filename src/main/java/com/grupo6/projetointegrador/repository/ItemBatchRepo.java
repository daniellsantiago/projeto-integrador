package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.entity.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.List;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {
    ItemBatch findByProductId(Long productId);
    
    List<ItemBatch> findAllByProductId(Long productId);
    
    List<ItemBatch> findAllByProductIdOrderByIdAsc(Long productId);
    
    List<ItemBatch> findAllByProductIdOrderByProductQuantityAsc(Long productId);
    
    List<ItemBatch> findAllByProductIdOrderByDueDateAsc(Long productId);
    /**
    * @param productId
    * @param productQuantity
    * @return Optional<ItemBatch>
    */
    @Query(value = "SELECT * FROM `item_batch` WHERE due_date > CURRENT_DATE + 21  AND product_id = (?1) " +
          "AND product_quantity >= (?2) ORDER BY due_date ASC LIMIT 1 ;", nativeQuery = true)
    Optional<ItemBatch> findByDueDate21AndProductIdAndQty(Long productId, int productQuantity);
}
