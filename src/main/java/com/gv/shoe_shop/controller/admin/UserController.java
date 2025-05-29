package com.gv.shoe_shop.controller.admin;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.request.UserAdminCreationRequest;
import com.gv.shoe_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/management-user")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUserByRoles("ROLE_" + StringConstant.USER));
        model.addAttribute("view", "users");
        model.addAttribute("childView", "management-user");
        return "admin/user/user-management";
    }

    @GetMapping("/create-admin")
    public String showCreateAdmin(Model model) {
        model.addAttribute("users", userService.getUserByRoles("ROLE_" + StringConstant.USER));
        model.addAttribute("view", "users");
        model.addAttribute("childView", "create-admin");
        return "admin/user/create-admin-account";
    }

    @GetMapping("/management-admin")
    public String showAdmins(Model model) {
        model.addAttribute("admin", userService.getUserByRoles("ROLE_" + StringConstant.ADMIN));
        model.addAttribute("view", "users");
        model.addAttribute("childView", "management-admin");
        return "admin/user/admin-management";
    }

    @PostMapping("/update-status")
    public String updateStatus(String id) {
        var role = userService.updateStatus(id);
        if(Objects.nonNull(role) && role.equals("ROLE_" + StringConstant.ADMIN)) {
            return "redirect:/admin/users/management-admin";
        }
        return "redirect:/admin/users/management-user";
    }

    @PostMapping("/create-admin")
    public String addAdmin(UserAdminCreationRequest request, RedirectAttributes redirectAttributes) {
        var user =userService.createAdminAccount(request, redirectAttributes);
        if(Objects.isNull(user)){
            return "redirect:/admin/users/create-admin";
        }
        return "redirect:/admin/users/management-admin";
    }

    @GetMapping("/search-user")
    public String showSearchUser(Model model, String query) {
        model.addAttribute("users",
                userService.search(query, new HashSet<>(List.of("ROLE_" + StringConstant.USER))));
        model.addAttribute("view", "users");
        model.addAttribute("childView", "management-user");
        return "admin/user/user-management";
    }

    @GetMapping("/search-admin")
    public String showSearchAdmin(Model model, String query) {
        model.addAttribute("admin",
                userService.search(query, new HashSet<>(List.of("ROLE_" + StringConstant.ADMIN))));
        model.addAttribute("view", "users");
        model.addAttribute("childView", "management-admin");
        return "admin/user/admin-management";
    }
}
