package com.gv.shoe_shop.config;


import com.gv.shoe_shop.constants.StringConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String errorMessage;
        if (exception.getMessage().contains(StringConstant.DISABLE)) {
            errorMessage = "Tài khoản đã bị vô hiệu hoá!";
        } else {
            errorMessage = "Tên đăng nhập hoặc mật khẩu không đúng!";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("login?error=true");
    }
}

