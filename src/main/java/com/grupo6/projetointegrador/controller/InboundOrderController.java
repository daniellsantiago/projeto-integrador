package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.service.InboundOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inboundorder")
public class InboundOrderController {

    private InboundOrderService service;

    public InboundOrderController(InboundOrderService service) {
        this.service = service;
    }

    @PostMapping
    public List<ItemBatchDto> createInboundOrder(@RequestBody CreateInboundOrderDto createInboundOrderDto){
        return service.createInboundOrder(createInboundOrderDto);
    }
}
