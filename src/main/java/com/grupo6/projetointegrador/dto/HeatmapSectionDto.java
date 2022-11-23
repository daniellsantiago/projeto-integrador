package com.grupo6.projetointegrador.dto;

public interface HeatmapSectionDto {
    Integer getTotalProduct();

    Integer getTotalSku();

    Long getSectionId();

    Double getSectionVolume();

    String getCategoryType();

    Double getCubeProduct();

    default String getVolumeCmCubicUsed() {
        // 0,00005 / 0.0041
        return String.format("%.2f", (getCubeProduct() / 1_000_000) * 100 / (getSectionVolume() / 1_000_000));
    }

    default String getVolumeCmCubicDisponible() {
        return String.format("%.2f", 100 - (getCubeProduct() / 1_000_000) * 100 / (getSectionVolume() / 1_000_000));
    }

    default String getVolumeLiterUsed() {
        return String.format("%.2f", ((getCubeProduct() / 1_000_000) * 1_000) * 100 / ((getSectionVolume() / 1_000_000) * 1_000));
    }
}