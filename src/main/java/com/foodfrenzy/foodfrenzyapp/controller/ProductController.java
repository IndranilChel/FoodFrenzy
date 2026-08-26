package com.foodfrenzy.foodfrenzyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foodfrenzy.foodfrenzyapp.services.ProductServices;

@Controller
public class ProductController {

    @Autowired
    private ProductServices service;


    // =========================================================
    // SHOW ALL PRODUCTS
    // =========================================================

    @GetMapping("/products")
    public String showAll(Model model) {

        model.addAttribute(
                "products",
                service.getAllProducts()
        );

        return "products";
    }


    // =========================================================
    // SHOW PRODUCTS BY CATEGORY
    // =========================================================

    @GetMapping("/category/{name}")
    public String showCategory(
            @PathVariable String name,
            Model model) {

        model.addAttribute(
                "products",
                service.getByCategory(name)
        );

        return "products";
    }
}