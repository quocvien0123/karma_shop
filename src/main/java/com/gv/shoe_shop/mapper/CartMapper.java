package com.gv.shoe_shop.mapper;

import com.gv.shoe_shop.dto.response.CartResponse;
import com.gv.shoe_shop.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);
}
