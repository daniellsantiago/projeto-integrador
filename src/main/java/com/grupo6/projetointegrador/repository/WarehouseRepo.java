package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.HeatmapSectionDto;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    @Query(value = "SELECT SUM(t1.volume) AS totalProduct, " +
            "COUNT(DISTINCT(t1.product_id)) AS totalSku, " +
            "t2.section_id AS sectionId, " +
            "t4.volume AS sectionVolume, " +
            "t4.category AS categoryType, " +
            "IFNULL( SUM( ( t5.ite_height * t5.ite_length * t5.ite_width ) * t1.product_quantity ), 0) AS cubeProduct " +
            "FROM item_batch t1 " +
            "LEFT JOIN inbound_order t2 ON t2.id = t1.inbound_order_id " +
            "LEFT JOIN section t4 ON t2.section_id = t4.id " +
            "LEFT JOIN product t5 ON t5.id = t1.product_id " +
            "LEFT JOIN warehouse t3 ON t3.id = t2.warehouse_id " +
            "WHERE t3.id = (?1) AND t1.product_quantity > 0 GROUP BY t2.section_id ORDER BY cubeProduct DESC;"
            , nativeQuery = true)
    List<HeatmapSectionDto> getWarehouseVolumeBySectionUsed(Long id);

    @Query(value = "SELECT IFNULL(SUM(t4.volume), 0) AS totalProduct, " +
            "COUNT(DISTINCT(t4.product_id)) AS totalSku, " +
            "t1.volume AS sectionVolume, " +
            "t1.category AS categoryType, " +
            "IFNULL( SUM( ( t5.ite_height * t5.ite_length * t5.ite_width ) * t4.product_quantity ), 0 ) AS cubeProduct, " +
            "t1.id AS sectionId " +
            "FROM section t1 " +
            "LEFT JOIN warehouse t2 ON t2.id = t1.warehouse_id " +
            "LEFT JOIN inbound_order t3 ON t3.section_id = t1.id " +
            "LEFT JOIN item_batch t4 ON t4.inbound_order_id = t3.id " +
            "LEFT JOIN product t5 ON t5.id = t4.product_id " +
            "WHERE t2.id = (?1) GROUP BY t1.id ORDER BY cubeProduct DESC;"
            , nativeQuery = true)
    List<HeatmapSectionDto> getWarehouseVolumeByAllSection(Long id);

    @Query(value = "SELECT IFNULL( SUM( ( t1.ite_height * t1.ite_length * t1.ite_width ) * t2.product_quantity ), 0 ) AS cubeProduct " +
            "FROM product t1 " +
            "LEFT JOIN item_batch t2 ON t2.product_id = t1.id " +
            "LEFT JOIN inbound_order t3 ON t3.id = t2.inbound_order_id " +
            "WHERE t3.section_id = (?1);"
            , nativeQuery = true)
    Double analyzeVolumeUsedInSection(Long id);

    @Query(value = "SELECT IFNULL(SUM(t2.volume), 0) AS totalProduct, " +
            "COUNT(DISTINCT(t2.product_id)) AS totalSku, " +
            "t4.volume AS sectionVolume, " +
            "t4.category AS categoryType, " +
            "t3.warehouse_id as warehouseId, " +
            " t4.id as sectionId, " +
            "IFNULL( SUM( ( t1.ite_height * t1.ite_length * t1.ite_width ) * t2.product_quantity ), 0 ) AS cubeProduct " +
            "FROM product t1 " +
            "LEFT JOIN item_batch t2 ON t2.product_id = t1.id " +
            "LEFT JOIN inbound_order t3 ON t3.id = t2.inbound_order_id " +
            "LEFT JOIN section t4 ON t3.section_id = t4.id " +
            "WHERE t4.id = (?1) HAVING t4.volume is not null;", nativeQuery = true)
    HeatmapSectionDto getVolumeBySection(Long id);
}