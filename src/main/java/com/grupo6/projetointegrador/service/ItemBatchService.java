package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.PatchItemBatchDto;

import java.util.Date;
import java.util.List;

public interface ItemBatchService {

    List<ItemBatchDto> getItemBatchesOutOfDate();

    ItemBatchDto patchItemBatch(Long itemBatchId, PatchItemBatchDto patchItemBatchDto);

    List<ItemBatchDto> getChangedBetweenTwoDates(String dateMin, String dateMax);
}
