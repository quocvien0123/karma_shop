package com.gv.shoe_shop.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class UserAdminCreationRequest {
    String name;
    LocalDate dob;
    String gender;
    String phoneNumber;
    String email;
    String address;
    String username;
    String password;
    String confirmPassword;
}
