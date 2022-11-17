package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.ProductOrderDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
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
   * This method finds an order by id, if it doesn't exist, it throws an exception, if it exists,
   * it converts the order's products to a list of ProductOrderDto and returns the order.
   *
   * @param id The id of the order to be found.
   * @return A DTO with the order and the products of the order or {@link NotFoundException} if none found.
   */
  public OrderPurchaseDto findById(Long id) {
    OrderPurchase orderPurchase = orderPurchaseRepo.findById(id).orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
    List<ProductOrderDto> productOrderDtos = orderPurchase.getProductOrders().stream().
            map(ProductOrderDto::fromProductOrder).collect(Collectors.toList());
    return OrderPurchaseDto.fromOrderPurchase(orderPurchase, productOrderDtos);
  }

  /**
   * The method receives an order id, finds the order, checks if it's open (Status Order equals "ABERTO"),
   * if it is, it sets the status to finalized, saves the order, and then updates the stock of the products in the order,
   * throws {@link NotFoundException} - if order purchased id not found.<p>
   * Also, check the {@link #setStock(ProductOrder)} method for more movement details.<p>
   *
   * @param id The id of the order to be finalized.
   * @return A String or {@link BusinessRuleException} - if status is closed (Status Order equals "FINALIZADO").
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
   * This method receives a DTO with a list of products and quantities, validates the products and quantities,
   * calculates the total cost of the order, and saves the order in the database.<p>
   * Also, check the {@link #findValidProduct(Long, int)} method for more movement details.<p>
   * Also, check the {@link #calculateTotalCost(ProductOrderDto)} method for more movement details.
   *
   * @param createOrderPurchaseDto This is the object that will be sent by the frontend.
   * @return A TotalPriceDto object with the total price of the order, a {@code double}.
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
   * Method to find a valid product by id and quantity, or throw an exception if it doesn't exist."<p>
   * The first thing we do is to call the repository's findByDueDateAndQty method, which returns an Optional.
   * Also, check the {@link ItemBatchRepo#findByDueDateAndQty(Long, int)} method for more movement details.
   *
   * @param id       the product id
   * @param quantity the quantity of the product that the user wants to buy
   * @return A list of ItemBatch objects or {@link NotFoundException} if none found.
   */
  public ItemBatch findValidProduct(Long id, int quantity) {
    return batchRepo.findByDueDateAndQty(id, quantity).orElseThrow(() -> new NotFoundException("Produto indisponível ou fora da validade."));
  }

  /**
   * Method to calculate the total cost of a product order (product price * productOrder quantity),
   * throws {@link NotFoundException} - if product order id not found.
   *
   * @param productOrder The product order that is being processed.
   */
  public void calculateTotalCost(ProductOrderDto productOrder) {
    Product product = productRepo.findById(productOrder.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    totalPrice += product.getPrice().doubleValue() * productOrder.getQuantity();
  }

  /**
   * Method to find a valid product, then update the quantity of that product (current quantity - productOrder quantity).<p>
   * Also, check the {@link #findValidProduct(Long, int)} method for more movement details.
   *
   * @param productOrder The product order object that contains the product id and quantity.
   */
  public void setStock(ProductOrder productOrder) {
    ItemBatch itemBatch = findValidProduct(productOrder.getProductId(), productOrder.getQuantity());

    int quantity = itemBatch.getProductQuantity() - productOrder.getQuantity();

    itemBatch.setProductQuantity(quantity);
    batchRepo.save(itemBatch);
  }
}