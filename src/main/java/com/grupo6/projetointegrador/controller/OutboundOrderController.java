package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateOutboundOrderDto;
import com.grupo6.projetointegrador.dto.OutboundItemBatchDto;
import com.grupo6.projetointegrador.dto.OutboundOrderDto;
import com.grupo6.projetointegrador.dto.UpdateOutboundItemBatchDto;
import com.grupo6.projetointegrador.service.OutboundOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/api/outbound-order")
@Validated
public class OutboundOrderController {

    private final OutboundOrderService service;

    public OutboundOrderController(OutboundOrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OutboundOrderDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<OutboundOrderDto> createOutboundOrder(@RequestBody @Valid CreateOutboundOrderDto createOutboundOrderDto){
        return new ResponseEntity<>(service.createOutboundOrder(createOutboundOrderDto), HttpStatus.CREATED);
    }

    /*@PutMapping("/{outboundOrderId}/item-batch")
    @ResponseStatus(HttpStatus.CREATED)
    public OutboundOrderDto updateOutboundItemBatches(
            @PathVariable Long outboundOrderId,
            @RequestBody @Valid @NotEmpty List<UpdateOutboundItemBatchDto> updateOutboundItemBatchDtos
    ) {
        return service.updateOutboundItemBatch(outboundOrderId, updateOutboundItemBatchDtos);
    }*/
}
