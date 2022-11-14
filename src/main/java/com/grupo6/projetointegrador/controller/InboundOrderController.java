package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/api/inboundorder")
@Validated
public class InboundOrderController {

    private final InboundOrderService service;

    public InboundOrderController(InboundOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<ItemBatchDto>> createInboundOrder(@RequestBody @Valid CreateInboundOrderDto createInboundOrderDto){
        return new ResponseEntity<>(service.createInboundOrder(createInboundOrderDto), HttpStatus.CREATED);
    }

    @PutMapping("/{inboundOrderId}/item-batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ItemBatchDto> updateItemBatches(
            @PathVariable Long inboundOrderId,
            @RequestBody @Valid @NotEmpty List<UpdateItemBatchDto> updateItemBatchDtos
    ) {
        return service.updateItemBatch(inboundOrderId, updateItemBatchDtos);
    }
}
