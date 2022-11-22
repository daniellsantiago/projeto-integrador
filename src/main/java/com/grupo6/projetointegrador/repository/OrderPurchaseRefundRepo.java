package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.ListRefundDto;
import com.grupo6.projetointegrador.model.entity.OrderPurchaseRefund;
import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderPurchaseRefundRepo extends JpaRepository<OrderPurchaseRefund, Long> {
    @Query(
            "SELECT new com.grupo6.projetointegrador.dto.ListRefundDto(" +
                    "opr.id, " +
                    "opr.orderPurchase.id, " +
                    "opr.reason, " +
                    "opr.refundDate)" +
            "FROM OrderPurchaseRefund opr " +
            "JOIN opr.orderPurchase.productOrders po " +
            "JOIN po.product p " +
            "WHERE (?1 IS NULL OR opr.orderPurchase.buyer.id = ?1) AND " +
                    "(?2 IS NULL OR opr.reason = :#{#refundReason}) AND " +
                    "(?3 IS NULL OR p.seller.id = (?3))"
    )
    List<ListRefundDto> findAllFiltered(Long buyerId, RefundReason refundReason, Long sellerId);
}
