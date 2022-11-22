package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;


import java.util.List;

public interface OutboundOrderService {
    List<OutboundItemBatchDto> createOutboundOrder(CreateOutboundOrderDto createInboundOrderDto);

    List<OutboundItemBatchDto> updateOutboundItemBatch(Long outboundOrderId, List<UpdateOutboundItemBatchDto> updateItemBatchDtos);
}
