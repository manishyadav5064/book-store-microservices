package com.example.notification_service.repository;

import com.example.notification_service.model.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
    boolean existsByEventId(String eventId);
}