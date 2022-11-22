package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    private final ItemBatchRepo itemBatchRepo;

    public ProductServiceImpl(ProductRepo productRepo, ItemBatchRepo itemBatchRepo) {
        this.productRepo = productRepo;
        this.itemBatchRepo = itemBatchRepo;
    }

    /**
     * This method returns a pageable response of fresh products.
     *
     * @param pageable This is the pageable object that contains the page number, page size, and sort information.
     * @return A PageableResponse object whit list of Product.
     */
    @Override
    public PageableResponse findPageableFreshProducts(Pageable pageable) {
        Page<Product> result = productRepo.findPageableProducts(pageable);
        return new PageableResponse().toResponse(result);
    }

    /**
     * This method returns a pageable response of products by category.
     *
     * @param pageable This is the pageable object that contains the page number, page size, and sort order.
     * @param category The category to search for
     * @return A PageableResponse object whit list of Product.
     */
    @Override
    public PageableResponse findProductsByCategory(Pageable pageable, Category category) {
        Page<Product> result = productRepo.findProductsByCategory(pageable, category);
        return new PageableResponse().toResponse(result);
    }

    /**
     * This method takes a product id and an order and returns a ProductLocationDto object.<p>
     * Also, check the {@link #verifyProductExists(Long)} method for more movement details.<p>
     * Also, check the {@link #findItemBatchByProductId(Long, String)} method for more movement details.<p>
     * Also, check the {@link #createSectionDto(List)} method for more movement details.
     *
     * @param productId The id of the product you want to find.
     * @param order     The order in which the item batches are sorted {@code String}.
     * @return A ProductLocationDto object.
     */
    @Override
    public ProductLocationDto findProductById(Long productId, String order) {
        verifyProductExists(productId);
        List<ItemBatch> itemBatchList = findItemBatchByProductId(productId, order);
        List<SectionDto> sectionDto = createSectionDto(itemBatchList);
        List<ItemBatchLocationDto> itemBatchLocationDto = itemBatchList.stream().map(ItemBatchLocationDto::fromItemBatch).collect(Collectors.toList());
        return new ProductLocationDto(sectionDto, productId, itemBatchLocationDto);
    }

    /**
     * Method to verify if the product with the given id does not exist,
     * throw a {@link NotFoundException} - if product id not found.
     *
     * @param productId The id of the product to be updated.
     */
    private void verifyProductExists(Long productId) {
        productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto com esse id não cadastrado."));
    }

    /**
     * Method to return a list of ItemBatch objects, ordered by the parameter passed to the function.
     *
     * @param productId The product id that you want to find the batches.
     * @param order     L = order by id, Q = order by quantity, V = order by due date
     * @return A list of ItemBatch objects or {@link NotFoundException} if ItemBatch list is empty.
     */
    private List<ItemBatch> findItemBatchByProductId(Long productId, String order) {
        List<ItemBatch> itemBatchList;
        switch (order) {
            case "L":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByIdAsc(productId);
                break;
            case "Q":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByProductQuantityAsc(productId);
                break;
            case "V":
                itemBatchList = itemBatchRepo.findAllByProductIdOrderByDueDateAsc(productId);
                break;
            default:
                itemBatchList = itemBatchRepo.findAllByProductId(productId);
                break;
        }
        if (itemBatchList.isEmpty()) {
            throw new NotFoundException("Lotes para esse produto não encontrados.");
        }
        return itemBatchList;
    }

    /**
     * Method to create a list of SectionDto.
     *
     * @param itemBatch the list of ItemBatch objects that we want to create the SectionDto objects from.
     * @return A list of SectionDto objects.
     */
    private List<SectionDto> createSectionDto(List<ItemBatch> itemBatch) {
        List<SectionDto> sectionDtoList = new ArrayList<>();

        List<Long> inboundOrderCovered = new ArrayList<>();

        itemBatch.forEach((batch) -> {
            Long batchInboundOrderId = batch.getInboundOrder().getId();

            if (!inboundOrderCovered.contains(batchInboundOrderId)) {
                SectionDto sectionDto = new SectionDto(batch.getInboundOrder().getSection().getId(),
                        batch.getInboundOrder().getWarehouse().getId());
                sectionDtoList.add(sectionDto);
                inboundOrderCovered.add(batchInboundOrderId);
            }
        });

        return sectionDtoList;
    }

    /**
     * Method to find a product in all warehouses.<p>
     * Also, check the {@link ProductRepo#findWarehousesByProduct(Long)} method for more movement details.
     *
     * @param id The id of the product
     * @return A list of warehouses that have the product with the given id or {@code null} if warehouse list is empty.
     */
    @Override
    public ProductWarehousesDto findProductWarehouse(Long id) {
        List<WarehouseDto> warehouses = productRepo.findWarehousesByProduct(id);
        if (warehouses.isEmpty()) {
            return null;
        }
        return new ProductWarehousesDto(id, warehouses);
    }
}