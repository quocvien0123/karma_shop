package com.gv.shoe_shop.service;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.dto.request.ChangePasswordRequest;
import com.gv.shoe_shop.dto.request.UserAdminCreationRequest;
import com.gv.shoe_shop.dto.request.UserUpdateProfileRequest;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.entity.User;
import com.gv.shoe_shop.mapper.UserMapper;
import com.gv.shoe_shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @NonFinal
    static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    public UserResponse register(String name,
                                 String email,
                                 String username,
                                 String password,
                                 String passwordConfirmation,
                                 RedirectAttributes model) {
        if(userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return null;
        }
        if(userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email đã tồn tại!");
            return null;
        }
        if(password.length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có độ dài ít nhất là 6 ký tự!");
            return null;
        }
        if(!password.equals(passwordConfirmation)) {
            model.addAttribute("error", "Mật khẩu nhập lại không đúng!");
            return null;
        }
        User user = User.builder()
                .name(name)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .status(StringConstant.ACTIVE)
                .createdDate(LocalDate.now())
                .updatedDate(LocalDate.now())
                .roles(new HashSet<>(List.of("ROLE_"+StringConstant.USER)))
                .image("avatar.jpg")
                .build();
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse findByUsername(String username){
        return userMapper.toUserResponse(userRepository.findByUsername(username).orElse(null));
    }



    public UserResponse createAdminAccount(UserAdminCreationRequest request, RedirectAttributes attributes){
        var user = userMapper.toUser(request);

        user.setName(request.getName());

        Period period = request.getDob().until(LocalDate.now());

        if(period.getYears() < 18){
            attributes.addFlashAttribute("error", "Ngày sinh không hợp lệ!");
            attributes.addFlashAttribute("model", request);
            return null;
        }

        user.setDob(request.getDob());

        user.setGender(request.getGender());

        if(request.getPhoneNumber().length() < 10){
            attributes.addFlashAttribute("model", request);
            attributes.addFlashAttribute("error", "Số điện thoại không hợp lệ!");
            return null;
        }
        user.setPhoneNumber(request.getPhoneNumber());

        user.setEmail(request.getEmail());

        user.setAddress(request.getAddress());

        if(userRepository.existsByUsername(user.getUsername())){
            attributes.addFlashAttribute("model", request);
            attributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return null;
        }

        user.setUsername(request.getUsername());

        if(!request.getConfirmPassword().equals(user.getPassword())){
            attributes.addFlashAttribute("model", request);
            attributes.addFlashAttribute("error", "Mật khẩu nhập lại không đúng!");
            return null;
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setImage("avatar.jpg");

        user.setStatus(StringConstant.ACTIVE);

        user.setCreatedDate(LocalDate.now());

        user.setUpdatedDate(LocalDate.now());

        user.setRoles(new HashSet<>(List.of("ROLE_"+StringConstant.ADMIN)));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateProfile(UserUpdateProfileRequest request, UserResponse userResponse, RedirectAttributes attributes) throws IOException {
        String imageUrl = saveImageInServer(request.getFile());

        var user = userMapper.toUser(userResponse);

        user.setName(request.getName());

        Period period = request.getDob().until(LocalDate.now());

        if(period.getYears() < 18){
            attributes.addFlashAttribute("error", "Ngày sinh không hợp lệ!");
            return null;
        }

        user.setDob(request.getDob());

        user.setGender(request.getGender());

        if(request.getPhoneNumber().length() < 10){
            attributes.addFlashAttribute("error", "Số điện thoại không hợp lệ!");
            return null;
        }
        user.setPhoneNumber(request.getPhoneNumber());

        user.setEmail(request.getEmail());

        user.setAddress(request.getAddress());

        user.setImage(Objects.isNull(imageUrl) ? userResponse.getImage() : imageUrl);

        user.setUpdatedDate(LocalDate.now());

        return userMapper.toUserResponse(userRepository.save(user));
    }
    public static String saveImageInServer(MultipartFile image) throws IOException {
        StringBuilder fileNames = new StringBuilder();

        if (!Objects.equals(image.getOriginalFilename(), "")) {
            File staticImageDir = ResourceUtils.getFile("classpath:static/assets/web/img/avatar");

            if (!staticImageDir.exists()) {
                staticImageDir.mkdirs();
            }

            Path resourcePath = staticImageDir.toPath();

            String imgName = UUID.randomUUID() + image.getOriginalFilename();
            Path fileNameAndPath = Paths.get(resourcePath.toString(), imgName);

            fileNames.append(imgName);
            Files.write(fileNameAndPath, image.getBytes());
            return fileNames.toString();
        }
        return null;
    }

    public List<UserResponse> getUserByRoles(String role){
        return userRepository.findByRoles(new HashSet<>(List.of(role))).stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public String updateStatus(String id){
        var user = userRepository.findById(id).orElse(null);
        assert user != null;
        user.setStatus(StringConstant.DISABLE.equals(user.getStatus())
                ? StringConstant.ACTIVE
                : StringConstant.DISABLE);
        user.setDeleteDate(LocalDate.now());
        return userMapper.toUserResponse(userRepository.save(user)).getRoles().stream().findFirst().orElseThrow(null);
    }

    public UserResponse updatePassword(ChangePasswordRequest request, UserResponse userResponse, RedirectAttributes attributes){
        var user = userMapper.toUser(userResponse);
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            attributes.addFlashAttribute("error", "Mật khẩu cũ và mật khẩu mới không trùng khớp!");
            return null;
        } else {
            if(!request.getNewPassword().equals(request.getConfirmPassword())){
                attributes.addFlashAttribute("error", "Mật khẩu mới và mật khẩu nhập lại không trùng khớp!");
                return null;
            } else {
                if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
                    attributes.addFlashAttribute("error", "Mật khẩu mới và mật khẩu cũ phải khác nhau!");
                    return null;
                } else {
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    user.setUpdatedDate(LocalDate.now());
                    return userMapper.toUserResponse(userRepository.save(user));
                }
            }
        }
    }

    public List<UserResponse> search(String username, Set<String> roles){
        return userRepository.findByRolesAndUsernameContainingIgnoreCase(roles ,username).stream()
                .map(userMapper::toUserResponse).collect(Collectors.toList());
    }


}
