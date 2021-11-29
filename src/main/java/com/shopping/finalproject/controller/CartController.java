package com.shopping.finalproject.controller;

import com.shopping.finalproject.global.GlobalData;
import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {
    @Autowired
    ProductsService productsService;
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable long id){
        GlobalData.cart.add(productsService.getProductById(id).get());
        return "redirect:/shop";
    }
    @GetMapping("/cart")
    public String cartGet(Model model){
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Products::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

}
