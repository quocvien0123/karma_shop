package com.gv.shoe_shop.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @MongoId
    private String id;  
    @DBRef
    private User user; 
    private LocalDate createdDate;

    private String status;

    private String address;

    private List<OrderDetail> orderDetails; // Embedded list of order details

    public static class OrderDetail {
        @DBRef
        private Product product; // Reference to Product

        private int quantity;
        private double price;
        public Product getProduct() {
            return product;
        }
        public int getQuantity() {
            return quantity;
        }
        public double getPrice() {
            return price;
        }
        public void setProduct(Product product) {
            this.product = product;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        public void setPrice(double price) {
            this.price = price;
        }

        
    }

}
