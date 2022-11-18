package com.grupo6.projetointegrador.dto;

import java.time.LocalDate;

public interface DueDateItemBatchDto {
    Long getItemBatchId();
    Long getProductId();
    String getCategory();
    LocalDate getDueDate();
    int getQuantity();
}
