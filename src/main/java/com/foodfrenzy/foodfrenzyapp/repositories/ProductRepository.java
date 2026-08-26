package com.foodfrenzy.foodfrenzyapp.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.foodfrenzy.foodfrenzyapp.entities.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{
    Optional<Product> findById(int id);
    List<Product> findByCategory(String category);

}
