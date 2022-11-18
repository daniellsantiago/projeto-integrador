package com.grupo6.projetointegrador.model.enumeration;

import lombok.Getter;

@Getter
public enum Category {
    FRESCO("FRESCO", "FS"),
    CONGELADO("CONGELADO", "FF"),
    REFRIGERADO("REFRIGERADO", "RF");

    private final String name;
    private final String code;

    Category(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static Category fromCode(String code) {
        for (Category category : Category.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}
