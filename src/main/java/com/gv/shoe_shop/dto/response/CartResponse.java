package com.gv.shoe_shop.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String id;

    private UserResponse user;

    private List<CartItemResponse> cartItems;


    public double totalMoney(){
        double sum = 0;
        for (CartResponse.CartItemResponse cartItem : cartItems) {
            sum += cartItem.getPrice() * cartItem.getQuantity();
        }
        return sum;
    }

    public static class CartItemResponse {
        private ProductResponse product;
        private int quantity;
        private double price;

        public CartItemResponse(ProductResponse product, int quantity, double price) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }

        public CartItemResponse() {
        }

        public ProductResponse getProduct() {
            return product;
        }

        public void setProduct(ProductResponse product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
