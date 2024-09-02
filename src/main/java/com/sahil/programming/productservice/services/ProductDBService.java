package com.sahil.programming.productservice.services;

import com.sahil.programming.productservice.ProductNotFoundException;
import com.sahil.programming.productservice.Repositories.CategoryRepository;
import com.sahil.programming.productservice.Repositories.ProductRepository;
import com.sahil.programming.productservice.models.Category;
import com.sahil.programming.productservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productDBService")
public class ProductDBService implements ProductService{
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductDBService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product not found");
        }

        return productOptional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String description, Double price,
                                 String imageUrl, String categoryName) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        product.setCategory(getCategoryByName(categoryName));

        return productRepository.save(product);
    }

    @Override
    public Product partialUpdate(Long id, Product product) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);


        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product Not Found");
        }
        Product productToUpdate = productOptional.get();
        if (product.getTitle()!=null){
            productToUpdate.setTitle(product.getTitle());
        }
        if (product.getDescription()!=null){
            productToUpdate.setDescription(product.getDescription());
        }
        if (product.getPrice()!=null){
            productToUpdate.setPrice(product.getPrice());
        }
        if (product.getImageUrl()!=null){
            productToUpdate.setImageUrl(product.getImageUrl());
        }
        if (product.getCategory()!=null){
            productToUpdate.setCategory(getCategoryByName(product.getCategory().getName()));
        }

        return productRepository.save(productToUpdate);
    }

    public Category getCategoryByName(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (categoryOptional.isEmpty()) {
            Category category = new Category();
            category.setName(categoryName);
            return category;
        }
        return categoryOptional.get();
    }
}
