package com.library.service;

import com.library.entity.Category;
import java.util.List;

public interface CategoryService {

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long id);

    List<Category> findAll();
}
