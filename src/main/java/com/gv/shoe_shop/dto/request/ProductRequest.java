package com.gv.shoe_shop.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequest {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private MultipartFile image;
    private String description;
    private String categoryid;

}
