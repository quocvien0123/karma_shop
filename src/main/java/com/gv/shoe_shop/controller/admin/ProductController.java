package com.gv.shoe_shop.controller.admin;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gv.shoe_shop.dto.request.ProductRequest;
import com.gv.shoe_shop.entity.Product;
import com.gv.shoe_shop.service.CategoryService;
import com.gv.shoe_shop.service.ProductService;


@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping()
    public String filterProduct(@RequestParam(value = "id", required = false) String categoryId, Model model) {
        List<Product> products;
        if (categoryId != null && !categoryId.equals("all")) {
            products = productService.getProductsByCategoryId(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        return "admin/product/index"; 
    }
    @GetMapping("/create")
    public String showCreateProduct(Model model){
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("view", "product");
        model.addAttribute("childView", "createProduct");
        return "admin/product/create";     
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductRequest productRequest){
        System.out.println(productRequest);
        try {
            productService.createProduct(productRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/products";

    }

    @GetMapping("/edit/{id}")
    public String showEditProduct(@PathVariable("id") String id, Model model){
        Product product = productService.getProductById(id);
        if(product != null){
            ProductRequest productDTO = new ProductRequest();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategoryid(product.getProductCategory().getId());

            model.addAttribute("productImg",product.getImage());
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product/edit";
        }else{
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") String id, @ModelAttribute ProductRequest productRequest){
        try {
            productService.updateProduct(id, productRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/products";
    }
    @ModelAttribute
    public void commonUser(Principal p, HttpSession session) {
        if (p != null) {
            String username = p.getName();
            UserResponse user = userService.findByUsername(username);
            if (user != null)
                session.setAttribute(StringConstant.USER, user);
        }
    }

    @GetMapping("/delete/{id}")
     public String softDeleteDevice(@PathVariable String id) {      
         productService.softDelete(id);
         return "redirect:/admin/products";
     }
}
