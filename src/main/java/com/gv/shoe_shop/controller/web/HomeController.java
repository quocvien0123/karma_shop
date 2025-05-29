package com.gv.shoe_shop.controller.web;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import com.gv.shoe_shop.service.ProductService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.getTopProducts());
        return "web/index";
    }

    @GetMapping("/home/details/{id}")
    public String detailsProduct(@PathVariable("id") String id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "web/detail";
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
}
