package com.foodfrenzy.foodfrenzyapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodfrenzy.foodfrenzyapp.entities.Cart;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.repositories.CartRepository;

@Service
public class CartServices {

    @Autowired
    private CartRepository cartRepository;


    // =========================================================
    // GET CART ITEMS FOR CURRENT USER
    // =========================================================

    public List<Cart> getCartItemsForUser(User user) {

        return cartRepository.findByUser(user);
    }


    // =========================================================
    // GET GRAND TOTAL FOR CURRENT USER
    // =========================================================

    public double getGrandTotal(User user) {

        double total = 0;

        List<Cart> cartItems =
                cartRepository.findByUser(user);

        for (Cart item : cartItems) {

            total += item.getTotalPrice();
        }

        return total;
    }


    // =========================================================
    // SAVE PRODUCT TO CART
    // =========================================================

    public void addToCart(Cart cart) {

        cartRepository.save(cart);
    }


    // =========================================================
    // REMOVE CART ITEM
    // =========================================================

    public void removeItem(int id) {

        cartRepository.deleteById(id);
    }


    // =========================================================
    // FIND CART ITEM
    // =========================================================

    public Cart getCartItem(int id) {

        return cartRepository
                .findById(id)
                .orElse(null);
    }


    // =========================================================
    // UPDATE CART ITEM
    // =========================================================

    public void updateCart(Cart cart) {

        cartRepository.save(cart);
    }


    // =========================================================
    // INCREASE QUANTITY
    // =========================================================

    public void increaseQuantity(int id) {

        Cart cart =
                cartRepository
                        .findById(id)
                        .orElse(null);

        if (cart != null) {

            cart.setQuantity(
                    cart.getQuantity() + 1
            );

            cart.setTotalPrice(
                    cart.getQuantity()
                            * cart.getProductPrice()
            );

            cartRepository.save(cart);
        }
    }


    // =========================================================
    // DECREASE QUANTITY
    // =========================================================

    public void decreaseQuantity(int id) {

        Cart cart =
                cartRepository
                        .findById(id)
                        .orElse(null);

        if (cart != null
                && cart.getQuantity() > 1) {

            cart.setQuantity(
                    cart.getQuantity() - 1
            );

            cart.setTotalPrice(
                    cart.getQuantity()
                            * cart.getProductPrice()
            );

            cartRepository.save(cart);
        }
    }


    // =========================================================
    // CLEAR CURRENT USER CART
    // =========================================================

    public void clearCart(User user) {

        List<Cart> cartItems =
                cartRepository.findByUser(user);

        cartRepository.deleteAll(cartItems);
    }
}