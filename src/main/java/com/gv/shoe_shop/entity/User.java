package com.gv.shoe_shop.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @MongoId
    private String id;
    private String name;
    private LocalDate dob;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String image;
    private String username;
    private String password;
    private String status;
    private Set<String> roles;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private LocalDate deleteDate;
}