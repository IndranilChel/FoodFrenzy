package com.foodfrenzy.foodfrenzyapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodfrenzy.foodfrenzyapp.entities.Product;
import com.foodfrenzy.foodfrenzyapp.repositories.ProductRepository;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository repo;


    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    public List<Product> getAllProducts() {

        return repo.findAll();
    }


    // =========================================================
    // GET PRODUCTS BY CATEGORY
    // =========================================================

    public List<Product> getByCategory(String category) {

        return repo.findByCategory(category);
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    public Product getProductById(int id) {

        return repo.findById(id).orElse(null);
    }


    // =========================================================
    // ADD PRODUCT
    // =========================================================

    public Product addProduct(Product product) {

        return repo.save(product);
    }


    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    public Product updateProduct(Product product) {

        return repo.save(product);
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    public void deleteProduct(int id) {

        repo.deleteById(id);
    }

}