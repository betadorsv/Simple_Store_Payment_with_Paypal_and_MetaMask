package com.shopping.finalproject.repository;

import com.shopping.finalproject.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findAllByCategoryId(int id);
}
