package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Orders;
import com.app.MediQuirk.model.OrderDetail;
import com.app.MediQuirk.repository.OrdersRepository;
import com.app.MediQuirk.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderDetailRepository orderDetailRepository) {
        this.ordersRepository = ordersRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public Orders createOrder(String orderDate, String totalAmount, String orderStatus,
                              Long userId, Long paymentMethodId, Set<OrderDetail> orderDetails) {
        Orders order = new Orders();
        order.setOrderDate(orderDate);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(orderStatus);

        // Set user and payment method (you might need to fetch these entities by their IDs)
        // order.setUser(userService.findById(userId));
        // order.setPaymentMethod(paymentMethodService.findById(paymentMethodId));

        // Set order details
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order);
        }
        order.setOrderDetails(orderDetails);

        // Save order and its details
        ordersRepository.save(order);

        return order;
    }
}