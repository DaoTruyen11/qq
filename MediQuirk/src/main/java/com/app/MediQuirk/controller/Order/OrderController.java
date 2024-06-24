package com.app.MediQuirk.controller.Order;

import com.app.MediQuirk.model.Orders;
import com.app.MediQuirk.model.OrderDetail;
import com.app.MediQuirk.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
@Controller
@RequestMapping("/cart") // Đảm bảo rằng cả hai controller đều sử dụng cùng prefix là /cart
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        // Populate model attributes if needed
        return "checkout"; // Thymeleaf template name
    }

    @PostMapping("/place-order") // Sửa đổi URL pattern tại đây
    public String placeOrder(@RequestParam String orderDate,
                             @RequestParam String totalAmount,
                             @RequestParam String orderStatus,
                             @RequestParam Long userId,
                             @RequestParam Long paymentMethodId,
                             @RequestParam int quantity,
                             @RequestParam String unitPrice,
                             @RequestParam String discount,
                             Model model) {
        // Create OrderDetail
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(quantity)
                .unitPrice(unitPrice)
                .discount(discount)
                .build();

        // Create a Set of OrderDetails
        Set<OrderDetail> orderDetails = new HashSet<>();
        orderDetails.add(orderDetail);

        // Create Order
        Orders order = orderService.createOrder(orderDate, totalAmount, orderStatus, userId, paymentMethodId, orderDetails);

        // Add order to model if needed
        model.addAttribute("order", order);

        // Redirect to success page
        return "redirect:/cart/order_success"; // Redirect to success page
    }

    @GetMapping("/success")
    public String orderSuccess(Model model) {
        // Add any additional model attributes if needed
        return "order_success"; // Thymeleaf template name for success page
    }
}