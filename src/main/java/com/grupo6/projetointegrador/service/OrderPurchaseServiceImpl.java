package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.ProductOrderDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.Buyer;
import com.grupo6.projetointegrador.model.OrderPurchase;
import com.grupo6.projetointegrador.model.ProductOrder;
import com.grupo6.projetointegrador.model.StatusOrder;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderPurchaseServiceImpl implements OrderPurchaseService {
  private OrderPurchaseRepo orderPurchaseRepo;
  private BuyerRepo buyerRepo;
  private ItemBatchRepo batchRepo;

  public OrderPurchaseServiceImpl(OrderPurchaseRepo orderPurchaseRepo, BuyerRepo buyerRepo, ItemBatchRepo batchRepo) {
    this.orderPurchaseRepo = orderPurchaseRepo;
    this.buyerRepo = buyerRepo;
    this.batchRepo = batchRepo;
  }

  public OrderPurchaseDto findById(Long id) {
    OrderPurchase orderPurchase = orderPurchaseRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
    List<ProductOrderDto> productOrderDtos = orderPurchase.getProductOrders().stream().
            map(ProductOrderDto::fromProductOrder).collect(Collectors.toList());
    return OrderPurchaseDto.fromOrderPurchase(orderPurchase, productOrderDtos);
  }

  public String endOrder(Long id) {
    OrderPurchase orderPurchase = orderPurchaseRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
    if(orderPurchase.getStatus().equals(StatusOrder.ABERTO)) {
      orderPurchase.setStatus(StatusOrder.FINALIZADO);
      orderPurchaseRepo.save(orderPurchase);
      return "Pedido finalizado com sucesso!";
    } else {
      throw new BusinessRuleException("Pedido já foi finalizado.");
    }
  }

  public String createOrderPurchase(CreateOrderPurchaseDto createOrderPurchaseDto) {
    System.out.println(createOrderPurchaseDto.toString());
    Buyer buyer = buyerRepo.findById(createOrderPurchaseDto.getBuyer()).orElseThrow(() -> new NotFoundException("Comprador não encontrado."));
    List<ProductOrder> productOrderDtos = createOrderPurchaseDto.getProductOrders().stream().
            map(ProductOrderDto::toProductOrder).collect(Collectors.toList());
    orderPurchaseRepo.save(CreateOrderPurchaseDto.toOrderPurchase(createOrderPurchaseDto, productOrderDtos, buyer));
    return "Pedido criado com sucesso!";
  }
}


