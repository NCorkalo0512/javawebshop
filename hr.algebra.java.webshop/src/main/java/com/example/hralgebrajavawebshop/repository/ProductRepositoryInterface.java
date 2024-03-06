package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
import com.example.hralgebrajavawebshop.models.CategoryProduct;
import com.example.hralgebrajavawebshop.models.ProductGrocery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryInterface extends JpaRepository<ProductGrocery,Integer> {

    List<ProductGrocery>findProductsByNameProduct(String nameProduct);
    List<ProductGrocery>findProductsByCategory(CategoryProduct category);

}
