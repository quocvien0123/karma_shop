package com.gv.shoe_shop.controller.web;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.entity.User;
import com.gv.shoe_shop.repository.UserRepository;
import com.gv.shoe_shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    UserService userService;
    UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register
    (
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "password-confirmation") String passwordConfirmation,
            RedirectAttributes  model,
            HttpSession session
    ) {
        var user = userService.register(name, email, username, password, passwordConfirmation, model);
        if (Objects.isNull(user)){
            return "redirect:/register";
        }
        session.setAttribute(StringConstant.USER, user);
        return "redirect:/home";
    }


}
