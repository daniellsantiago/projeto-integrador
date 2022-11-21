package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.PatchItemBatchDto;
import com.grupo6.projetointegrador.service.ItemBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/item-batch")
public class ItemBatchController {

    private final ItemBatchService itemBatchService;

    public ItemBatchController(ItemBatchService itemBatchService) {
        this.itemBatchService = itemBatchService;
    }

    @PatchMapping
    public ResponseEntity<ItemBatchDto> patchItemBatch(
            @RequestParam(name = "itemBatchId", required = true) Long itemBatchId,
            @RequestBody PatchItemBatchDto patchItemBatchDto
    ) {
        return new ResponseEntity<>(itemBatchService.patchItemBatch(itemBatchId, patchItemBatchDto), HttpStatus.OK);
    }

    @GetMapping("/audit/dueDate")
    public ResponseEntity<List<ItemBatchDto>> getItemBatchesOutOfDate(){
        return new ResponseEntity<>(itemBatchService.getItemBatchesOutOfDate(), HttpStatus.OK);
    }

    @GetMapping("/audit/changes")
    public ResponseEntity<List<ItemBatchDto>> getItemBatchesChangedBetweenTwoDates(
            @RequestParam(name = "dateMin", required = true) String dateMin,
            @RequestParam(name = "dateMax", required = true) String dateMax
            ){
        return new ResponseEntity<>(itemBatchService.getChangedBetweenTwoDates(dateMin, dateMax), HttpStatus.OK);
    }


}
