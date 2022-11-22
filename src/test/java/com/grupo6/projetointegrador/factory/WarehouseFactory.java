package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.Section;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.model.entity.WarehouseOperator;
import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.Setter;

import java.util.List;

@Setter
public class WarehouseFactory {
    private static Long id;
    private static List<Section> sections;
    private static WarehouseOperator warehouseOperator;

    public static Warehouse build() {
        Warehouse warehouse = new Warehouse();
        if (id == null) id = 1L;
        if (sections == null || sections.isEmpty()) sections = genericSections(warehouse);
        if (warehouseOperator == null) warehouseOperator = genericWarehouseOperator();

        warehouse.setId(id);
        warehouse.setSections(sections);
        warehouse.setWarehouseOperator(warehouseOperator);
        return warehouse;
    }

    private static List<Section> genericSections(Warehouse warehouse) {
        List<Section> sections = List.of(
                new Section(1L, warehouse, 200L, Category.FRESCO),
                new Section(2L, warehouse, 200L, Category.CONGELADO)
        );
        warehouse.setSections(sections);
        return sections;
    }

    private static WarehouseOperator genericWarehouseOperator() {
        return new WarehouseOperator(1L, null);
    }
}
