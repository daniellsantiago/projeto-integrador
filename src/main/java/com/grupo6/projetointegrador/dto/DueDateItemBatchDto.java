package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public interface DueDateItemBatchDto {
    Long getItemBatchId();
    Long getProductId();
    String getCategory();
    LocalDate getDueDate();
    int getQuantity();
}
