package com.sahil.programming.productservice.services;

import com.sahil.programming.productservice.ProductNotFoundException;
import com.sahil.programming.productservice.models.Product;

import java.util.List;

public interface ProductService {
    public Product getProductById(Long id) throws ProductNotFoundException;

    public List<Product> getAllProducts();

    public Product createProduct(String title, String description, Double price,
                              String imageUrl, String categoryName);

}
