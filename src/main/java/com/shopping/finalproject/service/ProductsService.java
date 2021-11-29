package com.shopping.finalproject.service;

import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.repository.CategoryRepository;
import com.shopping.finalproject.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    @Autowired
    ProductsRepository productsRepository;
    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }
    public void addProduct(Products products){
        productsRepository.save(products);
    }
    public void removeProductById(Long id){
        productsRepository.deleteById(id);
    }
    public Optional<Products> getProductById(Long id){
        return productsRepository.findById(id);
    }
    public List<Products> getAllProductsByCategoryId(int id){
        return productsRepository.findAllByCategoryId(id);
    }
}
