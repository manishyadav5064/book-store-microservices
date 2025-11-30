package com.example.order_service.repository;

import com.example.order_service.dto.OrderSummary;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderNumber(String s);

    List<Order> findByStatus(OrderStatus orderStatus);

    Optional<Order> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        Order order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        this.save(order);
    }

    @Query("""
            SELECT new com.example.order_service.dto.OrderSummary(o.orderNumber, o.status)
            FROM Order o
            WHERE o.username = :username
            """)
    List<OrderSummary> findOrderSummariesByUsername(String username);

    @Query("""
            SELECT distinct o
            FROM Order o left join fetch o.orderItems
            WHERE o.username = :username AND o.orderNumber = :orderNumber
            """)
    Optional<Order> findByUsernameAndOrderNumber(String username, String orderNumber);
}
