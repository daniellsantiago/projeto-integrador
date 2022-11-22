package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.Category;

import java.time.LocalDate;

public interface DueDateItemBatchDto {
    Long getItemBatchId();
    Long getProductId();
    Category getCategory();
    LocalDate getDueDate();
    int getQuantity();
}
