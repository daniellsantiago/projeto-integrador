package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.OutboundOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundOrderDto {
  private LocalDate orderDate;

  private List<OutboundItemBatchDto> itemBatches;

  public static OutboundOrderDto fromInboundOrder(InboundOrder outboundOrder) {
    return new OutboundOrderDto(
            outboundOrder.getOrderDate(),
            outboundOrder.getItemBatches().stream()
                    .map(OutboundItemBatchDto::fromItemBatch)
                    .collect(Collectors.toList())
    );
  }

  public static OutboundOrderDto fromOutboundOrder(OutboundOrder outboundOrder) {
    return new OutboundOrderDto(
            outboundOrder.getOrderDate(),
            outboundOrder.getOutboundItemBatches().stream()
                    .map(OutboundItemBatchDto::fromOutboundItemBatch)
                    .collect(Collectors.toList())
            );
  }
}