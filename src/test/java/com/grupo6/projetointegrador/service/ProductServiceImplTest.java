package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.dto.ProductWarehousesDto;
import com.grupo6.projetointegrador.dto.WarehouseDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.ItemBatchFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Section;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepo productRepo;

    @Mock
    private ItemBatchRepo itemBatchRepo;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findPageableFreshProducts_returnPageableResponse() {
        Mockito.when(productRepo.findPageableProducts(ArgumentMatchers.any()))
                .thenReturn(Page.empty());
        PageableResponse result = productService.findPageableFreshProducts(null);
        assertThat(result).isNotNull();
    }

    @Test
    void findProductsByCategory_returnPageableResponse() {
        Category category = Category.FRESCO;

        Mockito.when(productRepo.findProductsByCategory(ArgumentMatchers.any(), ArgumentMatchers.eq(category)))
                .thenReturn(Page.empty());
        PageableResponse result = productService.findProductsByCategory(null, category);
        assertThat(result).isNotNull();
    }

    @Test
    void findProductById_getProductLocationDtoOrderedByDueDateAsc_whenOrderValueIsV() {
        // Given
        Long productId = 1L;
        String order = "V";
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        ItemBatch itemBatch = ItemBatchFactory.build(InboundOrderFactory.build(section));

        // When
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        Mockito.when(itemBatchRepo.findAllByProductIdOrderByDueDateAsc(productId))
                .thenReturn(List.of(itemBatch));
        ProductLocationDto result = productService.findProductById(productId, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getSectionDto().get(0).getSectionId()).isEqualTo(section.getId());
        assertThat(result.getItemBatchLocationDto().get(0).getItemBatchId()).isEqualTo(itemBatch.getId());
    }

    @Test
    void findProductById_getProductLocationDtoOrderedByIdAsc_whenOrderValueIsL() {
        // Given
        Long productId = 1L;
        String order = "L";
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        ItemBatch itemBatch = ItemBatchFactory.build(InboundOrderFactory.build(section));

        // When
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        Mockito.when(itemBatchRepo.findAllByProductIdOrderByIdAsc(productId))
                .thenReturn(List.of(itemBatch));
        ProductLocationDto result = productService.findProductById(productId, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getSectionDto().get(0).getSectionId()).isEqualTo(section.getId());
        assertThat(result.getItemBatchLocationDto().get(0).getItemBatchId()).isEqualTo(itemBatch.getId());
    }

    @Test
    void findProductById_getProductLocationDtoOrderedByQuantityAsc_whenOrderValueIsQ() {
        // Given
        Long productId = 1L;
        String order = "Q";
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        ItemBatch itemBatch = ItemBatchFactory.build(InboundOrderFactory.build(section));

        // When
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        Mockito.when(itemBatchRepo.findAllByProductIdOrderByProductQuantityAsc(productId))
                .thenReturn(List.of(itemBatch));
        ProductLocationDto result = productService.findProductById(productId, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getSectionDto().get(0).getSectionId()).isEqualTo(section.getId());
        assertThat(result.getItemBatchLocationDto().get(0).getItemBatchId()).isEqualTo(itemBatch.getId());
    }

    @Test
    void findProductById_getProductLocationDtoByProductId_whenOrderIsNotValid() {
        // Given
        Long productId = 1L;
        String order = "UNKNOWN";
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        ItemBatch itemBatch = ItemBatchFactory.build(InboundOrderFactory.build(section));

        // When
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        Mockito.when(itemBatchRepo.findAllByProductId(productId))
                .thenReturn(List.of(itemBatch));
        ProductLocationDto result = productService.findProductById(productId, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getSectionDto().get(0).getSectionId()).isEqualTo(section.getId());
        assertThat(result.getItemBatchLocationDto().get(0).getItemBatchId()).isEqualTo(itemBatch.getId());
    }

    @Test
    void findProductById_throwsNotFoundException_whenProductDoesNotExists() {
        // Given
        Long productId = 1L;
        String order = "Q";

        // When / Then
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.findProductById(productId, order))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findProductById_throwsNotFoundException_whenItemBatchesDoesNotExists() {
        // Given
        Long productId = 1L;
        String order = "Q";

        // When / Then
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        Mockito.when(itemBatchRepo.findAllByProductIdOrderByProductQuantityAsc(productId))
                .thenReturn(List.of());
        assertThatThrownBy(() -> productService.findProductById(productId, order))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findProductWarehouse_getProductWarehousesDto_whenProductRepoFindsIt() {
        // Given
        Long productId = 1L;
        WarehouseDto warehouseDto = new WarehouseDto() {
            @Override
            public Long getWarehouse() {
                return 1L;
            }

            @Override
            public int getQuantity() {
                return 2;
            }
        };

        // When
        Mockito.when(productRepo.findWarehousesByProduct(productId))
                .thenReturn(List.of(warehouseDto));
        ProductWarehousesDto result = productService.findProductWarehouse(productId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void findProductWarehouse_getNull_whenProductRepoReturnsEmptyList() {
        // Given
        Long productId = 1L;

        // When
        Mockito.when(productRepo.findWarehousesByProduct(productId))
                .thenReturn(List.of());
        ProductWarehousesDto result = productService.findProductWarehouse(productId);

        // Then
        assertThat(result).isNull();
    }
}
