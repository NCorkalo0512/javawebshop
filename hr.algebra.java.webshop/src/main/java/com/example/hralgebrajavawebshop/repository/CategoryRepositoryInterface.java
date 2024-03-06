package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryInterface extends JpaRepository<CategoryProduct,Integer> {

    List<CategoryProduct>findCategoryByNameCategory(String nameCategory);

}
