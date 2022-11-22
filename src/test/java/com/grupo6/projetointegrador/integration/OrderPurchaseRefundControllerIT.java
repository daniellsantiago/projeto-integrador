package com.grupo6.projetointegrador.integration;

import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import com.grupo6.projetointegrador.repository.ItemBatchRepo;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import com.grupo6.projetointegrador.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderPurchaseRefundControllerIT {

    @Autowired
    private OrderPurchaseRepo orderPurchaseRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ItemBatchRepo itemBatchRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Test
    void refund_saveRefundAndUpdateOrderStatusAndUpdateItemStock_whenReasonIsArrependimentoAndDateOrderIsValid() {

    }

    private void createOrderPurchase(StatusOrder statusOrder) {
        List<ItemBatch> itemBatches = createProductsAndItemBatches();
        Buyer buyer = createBuyer();
        OrderPurchase orderPurchase = orderPurchaseRepo.save(
                new OrderPurchase(null, buyer, LocalDate.now(), List.of(), statusOrder)
        );
        ProductOrder productOrder1 = new ProductOrder(null, orderPurchase, itemBatches.get(0).getProduct(), 2);
        ProductOrder productOrder2 = new ProductOrder(null, orderPurchase, itemBatches.get(1).getProduct(), 1);
        orderPurchase.setProductOrders(List.of(productOrder1, productOrder2));
        orderPurchaseRepo.save(orderPurchase);
    }

    private List<ItemBatch> createProductsAndItemBatches() {
        Product product1 = productRepo.save(new Product(1L, BigDecimal.valueOf(10), Category.FRESCO, null));
        Product product2 = productRepo.save(new Product(2L, BigDecimal.valueOf(5), Category.FRESCO, null));
        return itemBatchRepo.saveAll(
                List.of(
                    new ItemBatch(
                        1L,
                        product1,
                        10,
                        LocalDate.now(),
                        LocalDateTime.now(),
                        20L,
                        LocalDate.of(2022, 12, 14),
                        BigDecimal.valueOf(100),
                        null,
                        product1.getCategory()
                    ),
                    new ItemBatch(
                            2L,
                            product2,
                            5,
                            LocalDate.now(),
                            LocalDateTime.now(),
                            20L,
                            LocalDate.of(2022, 12, 14),
                            BigDecimal.valueOf(100),
                            null,
                            product2.getCategory()
                    )
                )
        );
    }

    private Buyer createBuyer() {
        return buyerRepo.save(new Buyer(1L, List.of()));
    }
}
