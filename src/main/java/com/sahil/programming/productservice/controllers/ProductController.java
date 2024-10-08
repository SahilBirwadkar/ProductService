package com.sahil.programming.productservice.controllers;

import com.sahil.programming.productservice.ProductNotFoundException;
import com.sahil.programming.productservice.dtos.ProductRequestDto;
import com.sahil.programming.productservice.dtos.ProductResponseDto;
import com.sahil.programming.productservice.models.Category;
import com.sahil.programming.productservice.models.Product;
import com.sahil.programming.productservice.services.ProductDBService;
import com.sahil.programming.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    private final ProductDBService productDBService;
    private ProductService productService;

    @Autowired
    public ProductController(@Qualifier("productDBService") ProductService productService, ProductDBService productDBService) {
        this.productService = productService;
        this.productDBService = productDBService;
    }


    @GetMapping("/product/{id}")
    public ProductResponseDto getProductById(@PathVariable("id") Long id) throws ProductNotFoundException {
        Product product = productService.getProductById(id);
        return ProductResponseDto.from(product);
    }

    @GetMapping("/product")
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        List<ProductResponseDto> responseDtos = new ArrayList<>();
        for(Product product: products) {
            responseDtos.add(ProductResponseDto.from(product));
        }

        return responseDtos;
    }

    @PostMapping("/product")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = productService.createProduct(
                productRequestDto.getTitle(),
                productRequestDto.getDescription(),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl(),
                productRequestDto.getCategoryName()
        );

        return ProductResponseDto.from(product);
    }

    @PatchMapping("/product/{id}")
    public Product partialUpdateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto requestDto) throws ProductNotFoundException {
        Product product = new Product();
        product.setId(id);
        product.setTitle(requestDto.getTitle());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setImageUrl(requestDto.getImageUrl());

        Category category = new Category();
        category.setName(requestDto.getCategoryName());
        product.setCategory(category);

        Product updatedProduct = productService.partialUpdate(id,product);

        return updatedProduct;
    }

    // Only invoked in ProductController class.
    // If there is any other controller that throws NPE, this won't be called
//    @ExceptionHandler(NullPointerException.class)
//    public ErrorDto nullPointerExceptionHandler() {
//        ErrorDto errorDto = new ErrorDto();
//        errorDto.setMessage("Something went wrong");
//        errorDto.setStatus("FAILURE");
//
//        return errorDto;
//    }
}
