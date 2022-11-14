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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderPurchaseServiceImpl implements OrderPurchaseService {
  private final OrderPurchaseRepo orderPurchaseRepo;
  private final BuyerRepo buyerRepo;
  private final ItemBatchRepo batchRepo;

  public OrderPurchaseServiceImpl(OrderPurchaseRepo orderPurchaseRepo, BuyerRepo buyerRepo, ItemBatchRepo batchRepo) {
    this.orderPurchaseRepo = orderPurchaseRepo;
    this.buyerRepo = buyerRepo;
    this.batchRepo = batchRepo;
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
      productOrders.forEach(this::updateStock);

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
    AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
    Buyer buyer = buyerRepo.findById(createOrderPurchaseDto.getBuyer()).orElseThrow(() -> new NotFoundException("Comprador não encontrado."));

    OrderPurchase orderPurchase = new OrderPurchase();

    List<ProductOrder> productOrders = createOrderPurchaseDto.getProductOrders().stream().
            map(productOrderDto -> {
              Product product = findValidItemBatch(productOrderDto.getProductId(), productOrderDto.getQuantity())
                      .getProduct();
              totalPrice.set(totalPrice.get().add(calculateTotalCost(productOrderDto, product)));
              return productOrderDto.toProductOrder(orderPurchase, product);
            }).collect(Collectors.toList());

    orderPurchase.setDateOrder(createOrderPurchaseDto.getDateOrder());
    orderPurchase.setProductOrders(productOrders);
    orderPurchase.setBuyer(buyer);
    orderPurchase.setStatus(StatusOrder.ABERTO);

    orderPurchaseRepo.save(orderPurchase);

    return new TotalPriceDto(totalPrice.get().doubleValue());
  }

  private ItemBatch findValidItemBatch(Long productId, int quantity) {
      return batchRepo.findByDueDate21AndProductIdAndQty(productId, quantity)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
  }

  private BigDecimal calculateTotalCost(ProductOrderDto productOrderDto, Product product) {
      return product.getPrice().multiply(BigDecimal.valueOf(productOrderDto.getQuantity()));
  }

  private void updateStock(ProductOrder productOrder) {
      ItemBatch itemBatch = findValidItemBatch(productOrder.getProduct().getId(), productOrder.getQuantity());
      int quantity = itemBatch.getProductQuantity() - productOrder.getQuantity();
      itemBatch.setProductQuantity(quantity);
      batchRepo.save(itemBatch);
  }
}
