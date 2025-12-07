package com.example.notification_service.service.impl;

import com.example.notification_service.ApplicationProperties;
import com.example.notification_service.dto.OrderCancelledEvent;
import com.example.notification_service.dto.OrderCreatedEvent;
import com.example.notification_service.dto.OrderDeliveredEvent;
import com.example.notification_service.dto.OrderErrorEvent;
import com.example.notification_service.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final ApplicationProperties applicationProperties;


    @Override
    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        String message =
                """
                        ===================================================
                        Order Created Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been created successfully.
                        
                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Created Notification", message);
    }

    @Override
    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        String message =
                """
                        ===================================================
                        Order Delivered Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been delivered successfully.
                        
                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Delivered Notification", message);
    }

    @Override
    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        String message =
                """
                        ===================================================
                        Order Cancelled Notification
                        ----------------------------------------------------
                        Dear %s,
                        Your order with orderNumber: %s has been cancelled.
                        Reason: %s
                        
                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Cancelled Notification", message);
    }

    @Override
    public void sendOrderErrorNotification(OrderErrorEvent event) {
        String message =
                """
                        ===================================================
                        Order Processing Failure Notification
                        ----------------------------------------------------
                        Hi BookStore Team,
                        The order processing failed for orderNumber: %s.
                        Reason: %s
                        
                        Thanks,
                        BookStore Team
                        ===================================================
                        """
                        .formatted(event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(applicationProperties.supportEmail(), "Order Processing Failure Notification", message);
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(applicationProperties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            javaMailSender.send(mimeMessage);
            log.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}