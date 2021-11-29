package com.shopping.finalproject.controller;

import com.shopping.finalproject.global.GlobalData;
import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.service.CategoryService;
import com.shopping.finalproject.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShopController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductsService productsService;

    @GetMapping({"/", "/home"})
    public String home(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "index";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("cartCount",GlobalData.cart.size());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productsService.getAllProducts());
        return "shop";
    }
    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id){
        model.addAttribute("cartCount",GlobalData.cart.size());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productsService.getAllProductsByCategoryId(id));
        return "shop";
    }
    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable long id){
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("product", productsService.getProductById(id).get());
        return "viewProduct";
    }
    @GetMapping("/cart/removeItem/{index}")
    public String removeCartItem(@PathVariable int index){
        GlobalData.cart.remove(index);
        return "redirect:/cart";
    }
    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Products::getPrice).sum());
        return "checkout";
    }
}
