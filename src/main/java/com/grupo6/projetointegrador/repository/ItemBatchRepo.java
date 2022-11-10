package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.ItemBatch;
import com.grupo6.projetointegrador.model.OrderPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {
  @Query(value = "SELECT * FROM `item_batch` WHERE DATEDIFF(CURDATE(), due_date) < ?2 AND product_quantity >= ?1 AND product_id = ?3;",
          nativeQuery = true)

  List<ItemBatch> findByDueDateAndQty(int quantityProduct, int quantityDay, Long productId);
}
