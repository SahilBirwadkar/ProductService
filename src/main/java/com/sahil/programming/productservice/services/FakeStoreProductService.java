package com.sahil.programming.productservice.services;

import com.sahil.programming.productservice.ProductNotFoundException;
import com.sahil.programming.productservice.dtos.FakeStoreProductRequestDto;
import com.sahil.programming.productservice.dtos.FakeStoreProductResponseDto;
import com.sahil.programming.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakestoreservice")
public class FakeStoreProductService implements ProductService {

    private RestTemplate restTemplate;

    @Autowired
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        FakeStoreProductResponseDto responseDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + id,
                FakeStoreProductResponseDto.class
        );

        if(responseDto == null) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }

        return responseDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductResponseDto[] responseDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductResponseDto[].class
        );

        List<Product> products = new ArrayList<>();
        for(FakeStoreProductResponseDto responseDto: responseDtos) {
            products.add(responseDto.toProduct());
        }

        return products;
    }

    @Override
    public Product createProduct(String title, String description,
                              Double price, String imageUrl, String categoryName) {

        FakeStoreProductRequestDto requestDto = new FakeStoreProductRequestDto();
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setCategory(categoryName);
        requestDto.setPrice(price);
        requestDto.setImage(imageUrl);

        FakeStoreProductResponseDto responseDto = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                requestDto,
                FakeStoreProductResponseDto.class
        );


        return responseDto.toProduct();
    }

    @Override
    public Product partialUpdate(Long id, Product product) {
        return null;
    }
}
