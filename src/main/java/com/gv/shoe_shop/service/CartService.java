package com.gv.shoe_shop.service;

import com.gv.shoe_shop.dto.request.CartRequest;
import com.gv.shoe_shop.dto.response.CartResponse;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.entity.Cart;
import com.gv.shoe_shop.entity.User;
import com.gv.shoe_shop.mapper.CartMapper;
import com.gv.shoe_shop.mapper.UserMapper;
import com.gv.shoe_shop.repository.CartRepository;
import com.gv.shoe_shop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;
    UserMapper userMapper;

    public CartResponse getCartByUser(UserResponse user) {
        return cartMapper.toCartResponse(cartRepository.findByUserId(user.getId()).orElse(null));
    }

    public CartResponse saveCart(CartRequest cartRequest, UserResponse userResponse) {
        var cart = cartRepository.findByUserId(userResponse.getId()).orElseGet(Cart::new);

        var product = productRepository.findById(cartRequest.getProductId()).orElse(null);

        var user = userMapper.toUser(userResponse);

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        Cart.CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartRequest.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartRequest.getQuantity());
        } else {
            Cart.CartItem cartItem = new Cart.CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            cartItem.setPrice(cartRequest.getPrice());
            cart.getCartItems().add(cartItem);
        }

        cart.setUser(user);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public CartResponse updateCart(String id, String productId, int quantity){
        var cart = cartRepository.findById(id).orElseGet(Cart::new);
        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
        assert cartItem != null;
        if(quantity == 1){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else if(quantity == -1){
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            if(cartItem.getQuantity() < 1){
                cart.getCartItems().remove(cartItem);
            }  
        } else {
            cartItem.setQuantity(quantity);
            if(cartItem.getQuantity() < 1){
                cart.getCartItems().remove(cartItem);
            }
        }
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public void removeCart(String id, UserResponse userResponse){
        var cart = cartRepository.findByUserId(userResponse.getId()).orElseGet(Cart::new);
        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(id));
        cartRepository.save(cart);
    }

}
