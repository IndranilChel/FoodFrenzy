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


    // =========================================================
    // CHECKOUT PAGE
    // =========================================================

    @GetMapping("/checkout")
    public String checkoutPage(
            Model model,
            HttpSession session) {

        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // User must be logged in
        if (loggedInUser == null) {
            return "redirect:/login";
        }


        // Get ONLY this user's cart
        List<Cart> cartItems =
                cartService.getCartItemsForUser(loggedInUser);


        // Cart empty
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }


        // Get ONLY this user's total
        double grandTotal =
                cartService.getGrandTotal(loggedInUser);


        // Send cart data to checkout.html
        model.addAttribute(
                "cartItems",
                cartItems
        );

        model.addAttribute(
                "grandTotal",
                grandTotal
        );

        model.addAttribute(
                "user",
                loggedInUser
        );


        return "checkout";
    }


    // =========================================================
    // PLACE ORDER
    // =========================================================

    @PostMapping("/checkout/placeOrder")
    public String placeOrder(
            HttpSession session) {

        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // User must be logged in
        if (loggedInUser == null) {
            return "redirect:/login";
        }


        // Get ONLY this user's cart
        List<Cart> cartItems =
                cartService.getCartItemsForUser(loggedInUser);


        // Cart empty
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }


        // =====================================================
        // CREATE ORDER FOR EACH CART ITEM
        // =====================================================

        for (Cart item : cartItems) {

            Orders order = new Orders();


            // -------------------------------------------------
            // PRODUCT NAME
            // -------------------------------------------------

            order.setOrderName(
                    item.getProductName()
            );


            // -------------------------------------------------
            // PRODUCT PRICE
            // -------------------------------------------------

            order.setOrderPrice(
                    item.getProductPrice()
            );


            // -------------------------------------------------
            // QUANTITY
            // -------------------------------------------------

            order.setOrderQuantity(
                    item.getQuantity()
            );


            // -------------------------------------------------
            // TOTAL
            // -------------------------------------------------

            order.setTotalAmount(
                    item.getTotalPrice()
            );


            // -------------------------------------------------
            // ORDER DATE
            // -------------------------------------------------

            order.setOrderDate(
                    new Date()
            );


            // -------------------------------------------------
            // ORDER STATUS
            // -------------------------------------------------

            order.setOrderStatus(
                    "Pending"
            );


            // =================================================
            // IMPORTANT:
            // COPY PRODUCT IMAGE FROM CART TO ORDER
            // =================================================

            order.setProductImage(
                    item.getProductImage()
            );


            // -------------------------------------------------
            // ATTACH USER
            // -------------------------------------------------

            order.setUser(
                    loggedInUser
            );


            // -------------------------------------------------
            // SAVE ORDER
            // -------------------------------------------------

            orderService.saveOrder(
                    order
            );
        }


        // =====================================================
        // CLEAR ONLY THIS USER'S CART
        // =====================================================

        cartService.clearCart(
                loggedInUser
        );


        // =====================================================
        // ORDER SUCCESS
        // =====================================================

        return "redirect:/order-success";
    }


    // =========================================================
    // ORDER SUCCESS PAGE
    // =========================================================

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