package com.gv.shoe_shop.controller.admin;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.request.ChangePasswordRequest;
import com.gv.shoe_shop.dto.request.UserUpdateProfileRequest;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminAccountController {
    UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "admin/user/account/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(UserUpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        var user = userService.updateProfile(request, (UserResponse) session.getAttribute(StringConstant.USER), redirectAttributes);
        if(!Objects.isNull(user)) {
            session.removeAttribute(StringConstant.USER);
            session.setAttribute(StringConstant.USER, user);
        }
        return "redirect:/admin/profile";
    }

    @GetMapping("/change-password")
    public String showChangePassword(Model model) {
        return "admin/user/account/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordRequest request, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        var user = userService.updatePassword(request, (UserResponse) session.getAttribute(StringConstant.USER), redirectAttributes);
        if(!Objects.isNull(user)) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
            session.removeAttribute(StringConstant.USER);
            session.setAttribute(StringConstant.USER, user);
        }
        return "redirect:/admin/change-password";
    }
}
