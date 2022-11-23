package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;


import java.util.List;

public interface OutboundOrderService {
    OutboundOrderDto createOutboundOrder(CreateOutboundOrderDto createOutboundOrderDto);

//    OutboundOrderDto updateOutboundItemBatch(Long outboundOrderId, List<UpdateOutboundItemBatchDto> updateOutboundItemBatchDtos);

    List<OutboundOrderDto> getAll();
}
