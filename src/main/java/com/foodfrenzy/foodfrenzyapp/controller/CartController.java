package com.foodfrenzy.foodfrenzyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.foodfrenzy.foodfrenzyapp.entities.Cart;
import com.foodfrenzy.foodfrenzyapp.entities.Product;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.services.CartServices;
import com.foodfrenzy.foodfrenzyapp.services.ProductServices;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartServices cartService;

    @Autowired
    private ProductServices productService;


    // =========================================================
    // SHOW CART
    // =========================================================

    @GetMapping
    public String showCart(
            Model model,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "cartItems",
                cartService.getCartItemsForUser(loggedInUser)
        );

        model.addAttribute(
                "grandTotal",
                cartService.getGrandTotal(loggedInUser)
        );

        return "cart";
    }


    // =========================================================
    // ADD PRODUCT TO CART
    // =========================================================

    @GetMapping("/add/{id}")
    public String addToCart(
            @PathVariable int id,
            HttpSession session) {

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Product product =
                productService.getProductById(id);

        if (product != null) {

            Cart cart = new Cart();

            cart.setProductName(
                    product.getName()
            );

            cart.setProductPrice(
                    product.getPrice()
            );

            cart.setQuantity(1);

            cart.setTotalPrice(
                    product.getPrice()
            );

            cart.setProductImage(
                    product.getImage()
            );

            // Attach cart to logged-in user
            cart.setUser(loggedInUser);

            cartService.addToCart(cart);
        }

        return "redirect:/cart";
    }


    // =========================================================
    // REMOVE PRODUCT
    // =========================================================

    @GetMapping("/remove/{id}")
    public String removeItem(
            @PathVariable int id,
            HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        cartService.removeItem(id);

        return "redirect:/cart";
    }


    // =========================================================
    // INCREASE QUANTITY
    // =========================================================

    @GetMapping("/increase/{id}")
    public String increase(
            @PathVariable int id,
            HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        cartService.increaseQuantity(id);

        return "redirect:/cart";
    }


    // =========================================================
    // DECREASE QUANTITY
    // =========================================================

    @GetMapping("/decrease/{id}")
    public String decrease(
            @PathVariable int id,
            HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        cartService.decreaseQuantity(id);

        return "redirect:/cart";
    }
}