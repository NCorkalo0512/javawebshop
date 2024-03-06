package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.dto.CategoryProductDTO;
import com.example.hralgebrajavawebshop.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryProductRepository {
    private final CategoryRepositoryInterface categoryRepositoryInterface;

    @Autowired
    public CategoryProductRepository(CategoryRepositoryInterface categoryRepositoryInterface) {
        this.categoryRepositoryInterface = categoryRepositoryInterface;
    }


    public List<CategoryProduct> findAll() {
        return categoryRepositoryInterface.findAll();
    }

    public Optional<CategoryProduct> findById(Integer id) {
        return categoryRepositoryInterface.findById(id);
    }

    public List<CategoryProduct> findCategoryByName(String nameCategory) {
        return categoryRepositoryInterface.findCategoryByNameCategory( nameCategory);
    }

    public CategoryProduct addCategory(CategoryProductDTO categoryProductDTO) {
        CategoryProduct category = new CategoryProduct();
        category.setNameCategory(categoryProductDTO.getNameCategory());
        return categoryRepositoryInterface.save(category);
    }

    public CategoryProduct updateCategory(Integer categoryId, CategoryProductDTO categoryProductDTO) {
        CategoryProduct existingCategory = categoryRepositoryInterface.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setNameCategory(categoryProductDTO.getNameCategory());
        return categoryRepositoryInterface.save(existingCategory);
    }

    public void deleteCategory(Integer id) throws Exception {
        if (!categoryRepositoryInterface.existsById(id)) {
            throw new Exception("Category with id " + id + " not found");
        }
        categoryRepositoryInterface.deleteById(id);
    }
}
