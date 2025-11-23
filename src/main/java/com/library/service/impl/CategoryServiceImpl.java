package com.library.service.impl;

import com.library.entity.Category;
import com.library.exception.BusinessException;
import com.library.repository.CategoryRepository;
import com.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        // 检查分类名称是否已存在
        if (categoryRepository.findByName(category.getName()) != null) {
            throw new BusinessException("分类名称已存在");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Category existingCategory = findById(category.getId());

        // 如果修改了分类名称，检查是否重复
        if (!existingCategory.getName().equals(category.getName())) {
            if (categoryRepository.findByName(category.getName()) != null) {
                throw new BusinessException("分类名称已存在");
            }
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException("分类不存在");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("分类不存在"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
