package com.foodfrenzy.foodfrenzyapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import com.foodfrenzy.foodfrenzyapp.entities.Cart;
import com.foodfrenzy.foodfrenzyapp.entities.Orders;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.services.CartServices;
import com.foodfrenzy.foodfrenzyapp.services.OrderServices;

@Controller
public class CheckoutController {

    @Autowired
    private CartServices cartService;

    @Autowired
    private OrderServices orderService;

    // ==============================
    // CHECKOUT PAGE
    // ==============================
    @GetMapping("/checkout")
    public String checkoutPage(
            Model model,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // User must be logged in
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Get only this user's cart
        List<Cart> cartItems =
                cartService.getCartItemsForUser(loggedInUser);

        // Cart empty
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Calculate only this user's total
        double grandTotal =
                cartService.getGrandTotalForUser(loggedInUser);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("user", loggedInUser);

        return "checkout";
    }

    // ==============================
    // PLACE ORDER
    // ==============================
    @PostMapping("/checkout/placeOrder")
    public String placeOrder(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // User must be logged in
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Get only this user's cart
        List<Cart> cartItems =
                cartService.getCartItemsForUser(loggedInUser);

        // Cart empty
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Create order for each cart item
        for (Cart item : cartItems) {

            Orders order = new Orders();

            order.setOrderName(
                    item.getProductName()
            );

            order.setOrderPrice(
                    item.getProductPrice()
            );

            order.setOrderQuantity(
                    item.getQuantity()
            );

            order.setTotalAmount(
                    item.getTotalPrice()
            );

            order.setOrderDate(
                    new Date()
            );

            order.setOrderStatus(
                    "Pending"
            );

            order.setProductImage(
                    item.getProductImage()
            );

            // Attach logged-in user
            order.setUser(
                    loggedInUser
            );

            orderService.saveOrder(order);
        }

        // Clear only this user's cart
        cartService.clearCartForUser(loggedInUser);

        return "redirect:/order-success";
    }

    // ==============================
    // ORDER SUCCESS
    // ==============================
    @GetMapping("/order-success")
    public String orderSuccess(
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        return "Order_success";
    }
}