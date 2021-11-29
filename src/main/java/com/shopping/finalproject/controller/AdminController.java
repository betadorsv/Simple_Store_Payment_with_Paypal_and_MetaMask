package com.shopping.finalproject.controller;

import com.shopping.finalproject.dto.ProductsDTO;
import com.shopping.finalproject.model.Category;
import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.service.CategoryService;
import com.shopping.finalproject.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir=System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductsService productsService;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }
    @GetMapping("/admin/categories")
    public String getCate(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String getCateAdd(Model model){
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }
    @PostMapping("/admin/categories/add")
    public String postCateAdd(@ModelAttribute("category") Category category){
        categoryService.addCartCategory(category);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCate(@PathVariable int id){
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public String updateCate(@PathVariable int id, Model model){
        Optional<Category> category= categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        }
        else{
            return "404";
        }
    }


    //products
    @GetMapping("/admin/products")
    public String products(Model model){
        model.addAttribute("products", productsService.getAllProducts());
        return "products";
    }
    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO", new ProductsDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }
    @PostMapping("/admin/products/add")
    public String addProduct(@ModelAttribute("productDTO")ProductsDTO productsDTO,
                             @RequestParam("productImage")MultipartFile file,
                             @RequestParam("imgName") String imgName) throws IOException {
    Products products=new Products();
    products.setId(productsDTO.getId());
    products.setName(productsDTO.getName());
    products.setCategory(categoryService.getCategoryById(productsDTO.getCategoryId()).get());
    products.setPrice(productsDTO.getPrice());
    products.setWeight(productsDTO.getWeight());
    products.setDescription(productsDTO.getDescription());
    String imageUUID;
    if(!file.isEmpty()){
        imageUUID=file.getOriginalFilename();
        Path fileNameAndPath= Paths.get(uploadDir, imageUUID);
        Files.write(fileNameAndPath, file.getBytes());
    }
    else {
        imageUUID=imgName;
    }
    products.setImageName(imageUUID);
    productsService.addProduct(products);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productsService.removeProductById(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id,Model model){
        Products products= productsService.getProductById(id).get();
        ProductsDTO productsDTO=new ProductsDTO();
        productsDTO.setId(products.getId());
        productsDTO.setName(products.getName());
        productsDTO.setCategoryId(products.getCategory().getId());
        productsDTO.setPrice(products.getPrice());
        productsDTO.setWeight(products.getWeight());
        productsDTO.setDescription(products.getDescription());
        productsDTO.setImageName(products.getImageName());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productsDTO);
        return "productsAdd";
    }
}
