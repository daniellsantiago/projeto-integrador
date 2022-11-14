package com.grupo6.projetointegrador.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageType {
    FRESCO("FRESCO"),
    CONGELADO("CONGELADO"),
    REFRIGERADO("REFRIGERADO");

    private final String name;
}
