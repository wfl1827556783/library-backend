package com.library.controller;

import com.library.common.Result;
import com.library.dto.CategoryDTO;
import com.library.entity.Category;
import com.library.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        Category savedCategory = categoryService.addCategory(category);
        CategoryDTO result = convertToDTO(savedCategory);
        return Result.success(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Result<CategoryDTO> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setId(id);
        Category updatedCategory = categoryService.updateCategory(category);
        CategoryDTO result = convertToDTO(updatedCategory);
        return Result.success(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<CategoryDTO> getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        CategoryDTO result = convertToDTO(category);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDTO> result = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }
}



