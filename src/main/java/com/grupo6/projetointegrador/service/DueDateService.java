package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;

import java.util.List;

public interface DueDateService {
    List<DueDateItemBatchDto> findItemBatchBySection(Long sectionId, int days);

    List<DueDateItemBatchDto> findItemBatchByCategory(String category, int days, String order);
}
