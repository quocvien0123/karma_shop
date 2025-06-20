package com.gv.shoe_shop.controller.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final PayOS payOS;
    private final CartService cartService;

    public OrderController(PayOS payOS, CartService cartService) {
        this.payOS = payOS;
        this.cartService = cartService;
    }

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    @PostMapping("/create")
    public void createPayment(HttpServletResponse response,
                              @RequestParam String productName,
                              @RequestParam String description,
                              @RequestParam Double price
                              ) throws IOException {
        try {
            long orderCode = System.currentTimeMillis() % 1000000;

            ItemData item = ItemData.builder()
                    .name(productName)
                    .price(price.intValue())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(description)
                    .amount(price.intValue())
                    .item(item)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData checkout = payOS.createPaymentLink(paymentData);
            response.sendRedirect(checkout.getCheckoutUrl());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/web/cart?error=true");
        }
    }

    @GetMapping("/success")
    public String paymentSuccess(Model model, HttpSession session) {
        var user = (UserResponse) session.getAttribute(StringConstant.USER);
        if (user != null) {
            cartService.clearCartItemsByUserId(user.getId());
        }

        model.addAttribute("message", "Thanh toán thành công!");
        return "web/success";
    }




    @GetMapping("/cancel")
    public String paymentCancel(Model model) {
        model.addAttribute("message", "Bạn đã hủy thanh toán.");
        return "web/cancel";
    }
}

