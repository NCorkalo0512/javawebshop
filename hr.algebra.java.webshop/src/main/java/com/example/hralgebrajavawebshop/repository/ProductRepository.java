package com.example.hralgebrajavawebshop.repository;


import com.example.hralgebrajavawebshop.dto.ProductGroceryDTO;
import com.example.hralgebrajavawebshop.models.CategoryProduct;
import com.example.hralgebrajavawebshop.models.ProductGrocery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
  private final ProductRepositoryInterface productRepositoryInterface;

  @Autowired
  private CategoryRepositoryInterface categoryRepositoryInterface;

  @Autowired
    public ProductRepository(ProductRepositoryInterface productRepositoryInterface) {
        this.productRepositoryInterface = productRepositoryInterface;
    }

    public List<ProductGrocery>findAllProducts(){
       return productRepositoryInterface.findAll();
    }

    public Optional<ProductGrocery>findProductById(Integer id){
      return productRepositoryInterface.findById(id);
    }

    public ProductGrocery saveProduct(ProductGroceryDTO productGroceryDTO){
        ProductGrocery product = convertDtoToEntity(productGroceryDTO);
        return productRepositoryInterface.save(product);
    }

    public ProductGrocery updateProduct(Integer id, ProductGroceryDTO productGroceryDTO) {
        ProductGrocery product = convertDtoToEntity(productGroceryDTO);
        product.setIdProduct(id);
        return productRepositoryInterface.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepositoryInterface.deleteById(id);
    }


    public List<ProductGrocery> findProductsByName(String nameProduct) {
        return productRepositoryInterface.findProductsByNameProduct(nameProduct);
    }

    public List<ProductGrocery> findProductsByCategory(CategoryProduct category) {
        return productRepositoryInterface.findProductsByCategory(category);
    }

    private ProductGrocery convertDtoToEntity(ProductGroceryDTO productGroceryDTO) {
        ProductGrocery product = new ProductGrocery();
        product.setNameProduct(productGroceryDTO.getNameProduct());
        product.setDescription(productGroceryDTO.getDescription());
        product.setPrice(productGroceryDTO.getPrice());

        if (productGroceryDTO.getCategoryId() != null) {
            CategoryProduct category = categoryRepositoryInterface.findById(productGroceryDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return product;
    }
}
