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

    /**
     * This method returns a list of DueDateItemBatchDto objects, which are the result of a query.<p>
     * Also, check the {@link ItemBatchRepo#findByDueDateWithSectionId(Long, int)} method for more movement details.
     *
     * @param sectionId The id of the section you want to search for.
     * @param days      The number of days from the current date to search for batches.
     * @return A list of DueDateItemBatchDto objects.
     */
    @Override
    public List<DueDateItemBatchDto> findItemBatchBySection(Long sectionId, int days) {
        if (days < 0) {
            throw new BusinessRuleException("Intervalo de dias inválido. Precisa ser maior que zero.");
        }
        List<DueDateItemBatchDto> dueDateItemBatchDtos = itemBatchRepo.findByDueDateWithSectionId(sectionId, days);
        if (dueDateItemBatchDtos.isEmpty()) {
            throw new NotFoundException("Nenhum lote encontrado.");
        }
        return dueDateItemBatchDtos;
    }

    /**
     * This method finds all item batches that are due in the next X days, ordered by the given order, and filtered by the given
     * category.<p>
     * Also, check the {@link ItemBatchRepo#findByDueDateWithCategory(String, int, String)} method for more movement details.
     *
     * @param category The category of the item.
     * @param days     number of days to search for.
     * @param order    ASC or DESC.
     * @return A list of DueDateItemBatchDto.
     */
    @Override
    public List<DueDateItemBatchDto> findItemBatchByCategory(String category, int days, String order) {
        try {
            String categoryToSearch = Category.fromCode(category).getName();
            return itemBatchRepo.findByDueDateWithCategory(categoryToSearch, days, order.toLowerCase())
                    .orElseThrow(() -> new NotFoundException("Nenhum lote encontrado."));
        } catch (NullPointerException exception) {
            throw new BusinessRuleException("A categoria passada é inválida.");
        }

    }
}
