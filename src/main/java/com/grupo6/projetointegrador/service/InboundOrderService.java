package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;

import java.util.List;

public interface InboundOrderService {
    List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto);
}
