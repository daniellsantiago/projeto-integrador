package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.PatchItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemBatchServiceImpl implements ItemBatchService{

    private final ItemBatchRepo itemBatchRepo;

    public ItemBatchServiceImpl(ItemBatchRepo itemBatchRepo) {
        this.itemBatchRepo = itemBatchRepo;
    }

    /***
     * Search the repository for all out-of-date itemBatches and returns a list of them in DTO.
     * @return a list of itemBatches in DTO.
     */
    @Override
    public List<ItemBatchDto> getItemBatchesOutOfDate() {
        List<ItemBatch> itemBatchList = itemBatchRepo.findAllOutOfDate();
            if (itemBatchList.isEmpty()){
                throw new NotFoundException("Nenhum item com menos de 21 dias da validade encontrado.");
            }

        return itemBatchList.stream().map(ItemBatchDto::fromItemBatch).collect(Collectors.toList());
    }

    /***
     * Updates the itemBatch that is found according to the param ItemBatchId.
     * The updated information is the one passed in patchItemBatchDto,
     * with only 3 valid pieces of information: productQuantity, volume and/or price.
     * @param itemBatchId
     * @param patchItemBatchDtos
     * @return returns ItemBatch in DTO
     */
    @Override
    public ItemBatchDto patchItemBatch(Long itemBatchId, PatchItemBatchDto patchItemBatchDtos) {
        ItemBatch itemBatch = itemBatchRepo.findById(itemBatchId)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado."));

        Integer productQuantity = patchItemBatchDtos.getProductQuantity();
        Long volume = patchItemBatchDtos.getVolume();
        BigDecimal price = patchItemBatchDtos.getPrice();

        if (productQuantity != null){
            if (productQuantity > 0) {
                itemBatch.setProductQuantity(productQuantity);
            } else {
                throw new BusinessRuleException("A quantidade do produto deve ser maior que zero.");
            }
        }

        if (price != null){
            if (price.compareTo(BigDecimal.ZERO) > 0) {
                itemBatch.setPrice(price);
            } else {
                throw new BusinessRuleException("O preço deve ser maior que 0.");
            }
        }

        if (volume != null){
            if (volume.compareTo(0L) > 0){
                itemBatch.setVolume(volume);
            } else {
                throw new BusinessRuleException("O volume deve ser maior que zero.");
            }
        }

        itemBatch.setLastChangeDateTime(LocalDateTime.now());

        ItemBatch itemBatchSaved = itemBatchRepo.save(itemBatch);

        return ItemBatchDto.fromItemBatch(itemBatchSaved);
    }


    /**
     * Searches all batches that were changed during the period
     * that is passed in the parameter (between the minimum and maximum dates).
     * @param dateMin
     * @param dateMax
     * @return Returns a list of itemBatches changed between the given dates
     */
    @Override
    public List<ItemBatchDto> getChangedBetweenTwoDates(String dateMin, String dateMax) {

        List<ItemBatch> itemBatchList = itemBatchRepo.findAllBetweenTwoDates(dateMin, dateMax)
                .orElseThrow(() -> new NotFoundException("Nenhum lote encontrado para as datas informadas"));

        return itemBatchList.stream().map(ItemBatchDto::fromItemBatch).collect(Collectors.toList());
    }

}
