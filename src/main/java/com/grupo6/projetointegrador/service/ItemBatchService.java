package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.model.ItemBatch;

public interface ItemBatchService {
    ItemBatch createItemBatch(CreateItemBatchDto itemBatchDto);
}
