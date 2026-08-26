package com.foodfrenzy.foodfrenzyapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private double price;

    private String category;

    private String image;

    public Product(){}

    public int getId(){ return id; }

    public String getName(){ return name; }

    public double getPrice(){ return price; }

    public String getCategory(){ return category; }

    public String getImage(){ return image; }

    public void setImage(String image) {this.image = image;}

    public void setId(int id){ this.id=id; }

    public void setName(String name){ this.name=name; }

    public void setPrice(double price){ this.price=price; }

    public void setCategory(String category){ this.category=category; }


}
