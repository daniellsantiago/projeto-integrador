package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT t1.id AS itemBatchId," +
      "    t1.product_id AS productId," +
      "    t1.category as category," +
      "    t1.due_date AS dueDate," +
      "    t1.product_quantity AS quantity" +
      "    FROM item_batch AS t1" +
      "    LEFT JOIN inbound_order AS t2 ON t2.id = t1.inbound_order_id" +
      "    WHERE t2.section_id = ?1" +
      "    AND t1.product_quantity > 0" +
      "    AND due_date BETWEEN curdate() AND curdate() + interval ?2 day ORDER BY dueDate", nativeQuery = true)
    List<DueDateItemBatchDto> findByDueDateWithSectionId(Long sectionId, int days);

    @Query(value = "SELECT t1.id AS itemBatchId," +
            "    t1.product_id AS productId," +
            "    t1.category as category," +
            "    t1.due_date AS dueDate," +
            "    t1.product_quantity AS quantity" +
            "    FROM item_batch AS t1" +
            "    WHERE t1.category = ?1" +
            "    AND t1.product_quantity > 0" +
            "    AND due_date BETWEEN curdate() AND curdate() + interval ?2 day" +
            "    ORDER BY CASE WHEN ?3 = 'desc' THEN dueDate end DESC," +
            "    CASE WHEN ?3 != 'desc' THEN dueDate end ASC;", nativeQuery = true)
    Optional<List<DueDateItemBatchDto>> findByDueDateWithCategory(String category, int days, String order);

    @Query(value = "SELECT * FROM `item_batch` WHERE due_date < CURRENT_DATE + 21;", nativeQuery = true)
    List<ItemBatch> findAllOutOfDate();

    @Query(value = "SELECT * FROM item_batch" +
            "   WHERE DATE(last_change_date_time) BETWEEN ?1 AND ?2", nativeQuery = true)
    Optional<List<ItemBatch>> findAllBetweenTwoDates(String dateMin, String dateMax);
}
