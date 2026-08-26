package com.foodfrenzy.foodfrenzyapp.controller;

import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.foodfrenzy.foodfrenzyapp.entities.Orders;
import com.foodfrenzy.foodfrenzyapp.entities.Product;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.services.OrderServices;
import com.foodfrenzy.foodfrenzyapp.services.ProductServices;

@Controller
public class MyOrderController {

    @Autowired
    private OrderServices orderService;

    @Autowired
    private ProductServices productService;


    // =========================================================
    // MY ORDERS
    // =========================================================

    @GetMapping("/my-orders")
    public String myOrders(
            HttpSession session,
            Model model) {


        // =====================================================
        // GET LOGGED-IN USER
        // =====================================================

        User user =
                (User) session.getAttribute("loggedInUser");


        if (user == null) {

            return "redirect:/login";
        }


        // =====================================================
        // GET USER ORDERS
        // =====================================================

        List<Orders> orders =
                orderService.getOrdersForUser(user);


        // =====================================================
        // FIX MISSING PRODUCT IMAGES
        // =====================================================

        List<Product> products =
                productService.getAllProducts();


        for (Orders order : orders) {

            String image =
                    order.getProductImage();


            // Only repair orders whose image is missing

            if (image == null
                    || image.trim().isEmpty()
                    || image.equals("/images/logo.png")) {


                // Find matching product by name

                for (Product product : products) {

                    if (product.getName() != null
                            && order.getOrderName() != null
                            && product.getName()
                            .equalsIgnoreCase(
                                    order.getOrderName()
                            )) {


                        // Copy real product image

                        order.setProductImage(
                                product.getImage()
                        );


                        // Save repaired order

                        orderService.saveOrder(
                                order
                        );

                        break;
                    }
                }
            }
        }


        // =====================================================
        // NEWEST ORDER FIRST
        // =====================================================

        orders.sort(
                Comparator.comparing(
                        Orders::getOrderDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );


        // =====================================================
        // RECENT ORDER
        // =====================================================

        Orders recentOrder = null;

        if (!orders.isEmpty()) {

            recentOrder =
                    orders.get(0);
        }


        // =====================================================
        // SEND DATA TO PAGE
        // =====================================================

        model.addAttribute(
                "orders",
                orders
        );

        model.addAttribute(
                "recentOrder",
                recentOrder
        );

        model.addAttribute(
                "user",
                user
        );


        return "my-orders";
    }
}