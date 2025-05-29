package com.gv.shoe_shop.dto.response;

import com.gv.shoe_shop.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String image;
    private String description;
    private Category category;
}
