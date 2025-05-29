package com.gv.shoe_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class UserResponse {
    String id;
    String name;
    LocalDate dob;
    String gender;
    String email;
    String phoneNumber;
    String address;
    String image;
    String username;
    String password;
    String status;
    Set<String> roles;
    LocalDate createdDate;
    LocalDate updatedDate;
    LocalDate deleteDate;

}