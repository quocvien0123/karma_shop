package com.gv.shoe_shop.controller.web;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAccountController {
    UserService userService;

    @GetMapping("/profile")
    public String showUpdateProfile( ){
        return "web/user-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(UserUpdateProfileRequest request,
                                HttpSession session, RedirectAttributes attributes
    ) throws IOException {
        var user = userService.updateProfile(request,(UserResponse) session.getAttribute(StringConstant.USER), attributes);
        if(Objects.isNull(user)){
            return "redirect:/web/profile";
        }
        session.removeAttribute(StringConstant.USER);
        session.setAttribute(StringConstant.USER, user);
        return "web/user-profile";
    }

    @GetMapping("/change-password")
    public String showChangePassword( ){
        return "web/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordRequest request,
                                 HttpSession session, RedirectAttributes attributes
    ) throws IOException {
        var user = userService.updatePassword(request,(UserResponse) session.getAttribute(StringConstant.USER), attributes);
        if(!Objects.isNull(user)){
            attributes.addFlashAttribute("success", "Cập nhật thành công!");
            session.removeAttribute(StringConstant.USER);
            session.setAttribute(StringConstant.USER, user);
        }
        return "redirect:/web/change-password";
    }
}
