package com.gv.shoe_shop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gv.shoe_shop.dto.request.CategoryRequest;
import com.gv.shoe_shop.entity.Category;
import com.gv.shoe_shop.exception.CustomRuntimeException;
import com.gv.shoe_shop.repository.CategoryRepository;

@Service
public class CategoryService{
    
    @Autowired  
    private  CategoryRepository categoryRepository;

    public Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomRuntimeException("Category not found"));
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findByDeletedDateIsNull();
    }

    public Category createCategory(CategoryRequest categoryRequest){
        Category category = new Category();
        category.setId(categoryRequest.getId());
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setCreatedDate(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void softDeleteCategory(String id){
        try {
            Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CustomRuntimeException("Category not found with id: " + id));
            if (category != null) {
                category.setDeletedDate(LocalDateTime.now());
                categoryRepository.save(category);
        }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void updateCategory(String id, CategoryRequest categoryRequest){
        try {
            Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CustomRuntimeException("Category not found with id: " + id));
            if(category != null){
                category.setUpdatedDate(LocalDateTime.now());
                category.setDescription(categoryRequest.getDescription());
                category.setName(categoryRequest.getName());
                categoryRepository.save(category);
        }
        } catch (Exception e) {
            e.getStackTrace();
        }
        
    }
}
