package com.gv.shoe_shop.mapper;

import com.gv.shoe_shop.dto.request.UserAdminCreationRequest;
import com.gv.shoe_shop.dto.response.UserResponse;
import com.gv.shoe_shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toUser(UserResponse userResponse);
    User toUser(UserAdminCreationRequest request);
}
