package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;

import java.util.List;

public class BuyerFactory {

    private static Long id;

    private static List<OrderPurchase> orders;

    public static Buyer build(CreateBuyerDto createBuyerDto){
        Buyer buyer = new Buyer();

        if (id == null) id = 1L;
        if (orders == null || orders.isEmpty()) orders = List.of(genericOrder(buyer));

        buyer.setId(id);
        buyer.setName(createBuyerDto.getName());
        buyer.setCpf(createBuyerDto.getCpf());
        buyer.setAddress(createBuyerDto.getAddress());
        buyer.setNeighborhood(createBuyerDto.getNeighborhood());
        buyer.setCity(createBuyerDto.getCity());
        buyer.setState(createBuyerDto.getState());
        buyer.setZipCode(createBuyerDto.getZipCode());
        buyer.setActive(Active.ATIVO);

        return buyer;
    }

    public static OrderPurchase genericOrder(Buyer buyer){
        return new OrderPurchase(1L, buyer, null, null, StatusOrder.ABERTO);
    }
}
