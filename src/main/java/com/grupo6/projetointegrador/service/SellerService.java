package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.InactiveSellerBatchDto;
import com.grupo6.projetointegrador.model.entity.Seller;

import java.util.List;

public interface SellerService {
    Seller createSeller(CreateSellerDto createSellerDto);
    Seller findSeller(Long id);
    Seller updateSeller(Long id, CreateSellerDto createSellerDto);
    void deleteSeller(Long id);

    List<InactiveSellerBatchDto> getInactiveSellerBatches(Long id);
}
