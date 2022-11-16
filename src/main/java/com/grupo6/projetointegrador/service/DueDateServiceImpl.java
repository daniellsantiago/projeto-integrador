package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DueDateServiceImpl implements DueDateService{
    private final ItemBatchRepo itemBatchRepo;

    public DueDateServiceImpl(ItemBatchRepo itemBatchRepo) {
        this.itemBatchRepo = itemBatchRepo;
    }

    @Override
    public List<DueDateItemBatchDto> findItemBatchBySection(Long sectionId, int days) {
        if (days < 0){
            throw new BusinessRuleException("Intervalo de dias inválido. Precisa ser maior que zero.");
        }
        List<DueDateItemBatchDto> dueDateItemBatchDtos = itemBatchRepo.findByDueDateWithSectionId(sectionId, days);
        if (dueDateItemBatchDtos.isEmpty()){
            throw new NotFoundException("Nenhum lote encontrado.");
        }
        return dueDateItemBatchDtos;
    }

    @Override
    public List<DueDateItemBatchDto> findItemBatchByCategory(String category, int days, String order) {
        try {
            String categoryToSearch = Category.fromCode(category).getName();
            return itemBatchRepo.findByDueDateWithCategory(categoryToSearch, days, order.toLowerCase())
                    .orElseThrow(() -> new NotFoundException("Nenhum lote encontrado."));
        } catch (NullPointerException exception){
            throw new BusinessRuleException("A categoria passada é inválida.");
        }

    }
}
