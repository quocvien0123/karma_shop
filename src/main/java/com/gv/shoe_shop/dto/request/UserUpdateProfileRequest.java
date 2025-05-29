package com.gv.shoe_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateProfileRequest {
    String name;
    LocalDate dob;
    String gender;
    String phoneNumber;
    String email;
    String address;
    MultipartFile file;
}
