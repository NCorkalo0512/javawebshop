package com.example.hralgebrajavawebshop.controller;
import com.example.hralgebrajavawebshop.dto.CategoryProductDTO;
import com.example.hralgebrajavawebshop.models.CategoryProduct;
import com.example.hralgebrajavawebshop.repository.CategoryProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
@RequestMapping("/api/categories")
@AllArgsConstructor

public class CategoryController {

    private final CategoryProductRepository categoryProductRepository;

    @GetMapping
   public String listCategories(Model model){
        List<CategoryProduct>categoryProducts=categoryProductRepository.findAll();
        model.addAttribute("categoryProducts",categoryProducts);
        return "listCategories";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
   @GetMapping("/new")
   public String showNewCategoryForm(Model model){
        model.addAttribute("categoryDTO", new CategoryProductDTO());
        return "newCategory";
   }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
   @PostMapping
    public String addCategory(@ModelAttribute("categoryDTO") @Valid CategoryProductDTO categoryProductDTO ,BindingResult result ){
       if (result.hasErrors()) {
           return "/newCategory";
       }
       categoryProductRepository.addCategory(categoryProductDTO);
       return "redirect:/api/categories";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable Integer id, Model model) {
        CategoryProduct categoryProduct = categoryProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CategoryProductDTO categoryDTO = new CategoryProductDTO();
        categoryDTO.setNameCategory(categoryProduct.getNameCategory());
        categoryDTO.setIdCategory(categoryProduct.getIdCategory());

        model.addAttribute("categoryDTO", categoryDTO);
        return "editCategories";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Integer id, @ModelAttribute("categoryDTO")  @Valid CategoryProductDTO categoryProductDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/editCategories";
        }
        categoryProductRepository.updateCategory(id, categoryProductDTO);
        return "redirect:/api/categories";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) throws Exception {
        categoryProductRepository.deleteCategory(id);
        return "redirect:/api/categories";
    }
        /*KUPAC*/
        @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/search/by-name")
    public String searchCategoriesByName(@RequestParam("name") String name, Model model) {
        List<CategoryProduct> categoryProducts = categoryProductRepository.findCategoryByName(name);
        model.addAttribute("categories", categoryProducts);
        return "redirect:/listCategories";
    }



}
