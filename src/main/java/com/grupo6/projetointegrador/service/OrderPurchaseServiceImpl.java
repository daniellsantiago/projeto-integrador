package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.ProductOrderDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.model.entity.OrderPurchase;
import com.grupo6.projetointegrador.model.entity.ProductOrder;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderPurchaseServiceImpl implements OrderPurchaseService {
  private OrderPurchaseRepo orderPurchaseRepo;
  private BuyerRepo buyerRepo;
  private ItemBatchRepo batchRepo;
  private ProductRepo productRepo;
  private double totalPrice = 0;

  public OrderPurchaseServiceImpl(OrderPurchaseRepo orderPurchaseRepo, BuyerRepo buyerRepo, ItemBatchRepo batchRepo, ProductRepo productRepo) {
    this.orderPurchaseRepo = orderPurchaseRepo;
    this.buyerRepo = buyerRepo;
    this.batchRepo = batchRepo;
    this.productRepo = productRepo;
  }

  /**
   * Find OrderPurchase by id
   *
   * @param id
   * @return OrderPurchaseDto
   */
  public OrderPurchaseDto findById(Long id) {
    OrderPurchase orderPurchase = orderPurchaseRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
    List<ProductOrderDto> productOrderDtos = orderPurchase.getProductOrders().stream().
            map(ProductOrderDto::fromProductOrder).collect(Collectors.toList());
    return OrderPurchaseDto.fromOrderPurchase(orderPurchase, productOrderDtos);
  }

  /**
   * End order if status order open
   *
   * @param id
   * @return string or throw exception
   */
  @Transactional
  public String endOrder(Long id) {
    OrderPurchase orderPurchase = orderPurchaseRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
    if (orderPurchase.getStatus().equals(StatusOrder.ABERTO)) {
      orderPurchase.setStatus(StatusOrder.FINALIZADO);
      orderPurchaseRepo.save(orderPurchase);

      List<ProductOrder> productOrders = orderPurchase.getProductOrders();
      productOrders.forEach(this::setStock);

      return "Pedido finalizado com sucesso!";
    } else {
      throw new BusinessRuleException("Pedido já foi finalizado.");
    }
  }

  /**
   * Create order purchase
   *
   * @param createOrderPurchaseDto
   * @return total price
   */
  @Transactional
  public TotalPriceDto createOrderPurchase(CreateOrderPurchaseDto createOrderPurchaseDto) {
    totalPrice = 0;
    Buyer buyer = buyerRepo.findById(createOrderPurchaseDto.getBuyer()).orElseThrow(() -> new NotFoundException("Comprador não encontrado."));

    OrderPurchase orderPurchase = new OrderPurchase();

    List<ProductOrder> productOrderDtos = createOrderPurchaseDto.getProductOrders().stream().
            map(productOrder -> {
              findValidProduct(productOrder.getProductId(), productOrder.getQuantity());
              calculateTotalCost(productOrder);
              return ProductOrderDto.toProductOrder(productOrder, orderPurchase);
            }).collect(Collectors.toList());

    orderPurchase.setDateOrder(createOrderPurchaseDto.getDateOrder());
    orderPurchase.setProductOrders(productOrderDtos);
    orderPurchase.setBuyer(buyer);
    orderPurchase.setStatus(createOrderPurchaseDto.getStatus());

    orderPurchaseRepo.save(orderPurchase);

    return new TotalPriceDto(totalPrice);
  }

  /**
   * Find product using query native
   *
   * @param id
   * @param quantity
   * @return itemBatch or NotFoundException
   **/
  public ItemBatch findValidProduct(Long id, int quantity) {
    return batchRepo.findByDueDateAndQty(id, quantity).orElseThrow(() -> new NotFoundException("Produto indisponível ou fora da validade."));
  }

  /**
   * Calculate total cost in order purchase (quantity * price)
   *
   * @param productOrder
   */
  public void calculateTotalCost(ProductOrderDto productOrder) {
    Product product = productRepo.findById(productOrder.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    totalPrice += product.getPrice().doubleValue() * productOrder.getQuantity();
  }

  /**
   * Set new stock
   *
   * @param productOrder
   */
  public void setStock(ProductOrder productOrder) {
    ItemBatch itemBatch = findValidProduct(productOrder.getProductId(), productOrder.getQuantity());

    int quantity = itemBatch.getProductQuantity() - productOrder.getQuantity();

    itemBatch.setProductQuantity(quantity);
    batchRepo.save(itemBatch);
  }
}


