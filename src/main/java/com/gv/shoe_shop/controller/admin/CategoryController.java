package com.gv.shoe_shop.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gv.shoe_shop.dto.request.CategoryRequest;
import com.gv.shoe_shop.entity.Category;
import com.gv.shoe_shop.service.CategoryService;


@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String viewCategories(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("view", "category");
        model.addAttribute("childView", "allCategory");
        return "admin/category/index";
    }

    @GetMapping("/create")
    public String showCreateCategory(Model model){
        model.addAttribute("category", new Category());
        model.addAttribute("view", "category");
        model.addAttribute("childView", "createCategory");
        return "admin/category/create";   
    }   

    @PostMapping("/create")
    public String createCategory(@ModelAttribute CategoryRequest categoryRequest){
        categoryService.createCategory(categoryRequest);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategory(@PathVariable("id") String id, Model model){
        Category category = categoryService.getCategoryById(id);
        if(category != null){
            model.addAttribute("category", category);   
            return "admin/category/edit";
        }else{
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") String id, @ModelAttribute CategoryRequest categoryRequest){
        categoryService.updateCategory(id, categoryRequest);
        return "redirect:/admin/categories";

    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") String id){
        categoryService.softDeleteCategory(id);
        return "redirect:/admin/categories";
    }

}
