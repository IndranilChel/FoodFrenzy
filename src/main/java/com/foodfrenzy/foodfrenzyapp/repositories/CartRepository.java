package com.foodfrenzy.foodfrenzyapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodfrenzy.foodfrenzyapp.entities.Cart;
import com.foodfrenzy.foodfrenzyapp.entities.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    // Get only the cart items belonging to a specific user
    List<Cart> findByUser(User user);

}