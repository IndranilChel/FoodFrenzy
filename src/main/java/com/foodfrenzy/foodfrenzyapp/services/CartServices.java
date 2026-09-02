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

    // ==============================
    // SAVE CART ITEM
    // ==============================
    public void addToCart(Cart cart) {
        cartRepository.save(cart);
    }

    // ==============================
    // GET USER CART
    // ==============================
    public List<Cart> getCartItemsForUser(User user) {
        return cartRepository.findByUser(user);
    }

    // ==============================
    // GRAND TOTAL FOR USER
    // ==============================
    public double getGrandTotalForUser(User user) {

        double total = 0;

        List<Cart> cartItems =
                cartRepository.findByUser(user);

        for (Cart item : cartItems) {
            total += item.getTotalPrice();
        }

        return total;
    }

    // ==============================
    // REMOVE ITEM
    // ==============================
    public void removeItemForUser(
            int id,
            User user) {

        Cart cart =
                cartRepository.findById(id)
                        .orElse(null);

        if (cart != null
                && cart.getUser() != null
                && cart.getUser().getU_id() == user.getU_id()) {

            cartRepository.deleteById(id);
        }
    }

    // ==============================
    // INCREASE QUANTITY
    // ==============================
    public void increaseQuantityForUser(
            int id,
            User user) {

        Cart cart =
                cartRepository.findById(id)
                        .orElse(null);

        if (cart != null
                && cart.getUser() != null
                && cart.getUser().getU_id() == user.getU_id()) {

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

    // ==============================
    // DECREASE QUANTITY
    // ==============================
    public void decreaseQuantityForUser(
            int id,
            User user) {

        Cart cart =
                cartRepository.findById(id)
                        .orElse(null);

        if (cart != null
                && cart.getUser() != null
                && cart.getUser().getU_id() == user.getU_id()
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

    // ==============================
    // CLEAR USER CART
    // ==============================
    public void clearCartForUser(User user) {

        List<Cart> cartItems =
                cartRepository.findByUser(user);

        cartRepository.deleteAll(cartItems);
    }
}