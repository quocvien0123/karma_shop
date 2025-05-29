package com.gv.shoe_shop.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @MongoId
    private String id; 
    private String name;
    private double price;
    private int quantity;
    private String image;
    private String description;
    private LocalDateTime deletedDate ;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @DBRef
    private Category productCategory;

}

