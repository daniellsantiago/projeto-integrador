package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.Buyer;
import com.grupo6.projetointegrador.model.OrderPurchase;
import com.grupo6.projetointegrador.model.ProductOrder;
import com.grupo6.projetointegrador.model.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderPurchaseDto {
  private Long buyer;
  private LocalDate dateOrder;
  private StatusOrder status;
  private List<ProductOrderDto> productOrders;
  public static OrderPurchase toOrderPurchase(CreateOrderPurchaseDto createdOrderPurchase, List<ProductOrder> productOrder, Buyer buyer) {
    return new OrderPurchase(
            buyer,
            createdOrderPurchase.getDateOrder(),
            productOrder,
            createdOrderPurchase.getStatus()   );
  }
}
