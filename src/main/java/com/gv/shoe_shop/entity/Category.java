package com.gv.shoe_shop.entity;

import lombok.*;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {
    @MongoId
    private String id;  
    private String name;
    private String description;
    private LocalDateTime deletedDate ;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
