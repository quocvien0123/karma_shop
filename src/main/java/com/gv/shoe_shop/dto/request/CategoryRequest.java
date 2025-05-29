package com.gv.shoe_shop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryRequest {
    private String id;  
    private String name;
    private String description;
}
