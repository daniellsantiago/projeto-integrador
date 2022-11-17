package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class DueDateServiceImplTest {

    @Mock
    private ItemBatchRepo itemBatchRepo;

    @InjectMocks
    private DueDateServiceImpl dueDateService;

    @Test
    void findItemBatchBySection_getItems_whenIntervalIsPositiveAndItemsAreNotEmpty() {
        // Given
        Long sectionId = 1L;
        int days = 5;
        DueDateItemBatchDto dueDateItemBatchDto = genericDueDateItemBatchDto();

        // When
        Mockito.when(itemBatchRepo.findByDueDateWithSectionId(sectionId, days))
                .thenReturn(List.of(dueDateItemBatchDto));
        List<DueDateItemBatchDto> result = dueDateService.findItemBatchBySection(sectionId, days);

        // Then
        assertThat(result).isNotEmpty();
    }

    @Test
    void findItemBatchBySection_throwsBusinessException_whenDaysIsNegative() {
        // Given
        Long sectionId = 1L;
        int days = -1;

        // When / Then
        assertThatThrownBy(() -> dueDateService.findItemBatchBySection(sectionId, days))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void findItemBatchBySection_throwsNotFoundException_whenResultIsEmpty() {
        Long sectionId = 1L;
        int days = 5;

        // When / Then
        Mockito.when(itemBatchRepo.findByDueDateWithSectionId(sectionId, days))
                .thenReturn(List.of());
        assertThatThrownBy(() -> dueDateService.findItemBatchBySection(sectionId, days))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findItemBatchByCategory_getItems_whenResultIsNotNullAndCategoryIsValid() {
        // Given
        String categoryCode = Category.FRESCO.getCode();
        String categoryName = Category.FRESCO.getName();
        int days = 5;
        String order = "asc";
        DueDateItemBatchDto dueDateItemBatchDto = genericDueDateItemBatchDto();

        // When
        Mockito.when(itemBatchRepo.findByDueDateWithCategory(categoryName, days, order))
                .thenReturn(Optional.of(List.of(dueDateItemBatchDto)));
        List<DueDateItemBatchDto> result = dueDateService.findItemBatchByCategory(categoryCode, days, order);

        // Then
        assertThat(result).isNotEmpty();
    }

    @Test
    void findItemBatchByCategory_throwsBusinessException_whenCategoryIsNotValid() {
        // Given
        String invalidCode = "INVALID_CODE";
        int days = 5;
        String order = "asc";

        // When / Then
        assertThatThrownBy(() -> dueDateService.findItemBatchByCategory(invalidCode, days, order))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void findItemBatchByCategory_throwsNotFoundException_whenResultIsNull() {
        // Given
        String categoryCode = Category.FRESCO.getCode();
        int days = 5;
        String order = "asc";

        // When / Then
        assertThatThrownBy(() -> dueDateService.findItemBatchByCategory(categoryCode, days, order))
                .isInstanceOf(NotFoundException.class);
    }

    private DueDateItemBatchDto genericDueDateItemBatchDto() {
        return new DueDateItemBatchDto() {
            @Override
            public Long getItemBatchId() {
                return 1L;
            }

            @Override
            public Long getProductId() {
                return 1L;
            }

            @Override
            public String getCategory() {
                return "FRESCO";
            }

            @Override
            public LocalDate getDueDate() {
                return LocalDate.now();
            }

            @Override
            public int getQuantity() {
                return 5;
            }
        };
    }
}
