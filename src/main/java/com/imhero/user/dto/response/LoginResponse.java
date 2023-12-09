package com.imhero.user.dto.response;

import com.imhero.user.domain.User;
import lombok.Data;

@Data
public class LoginResponse {

    private Long id;

    private LoginResponse(Long id) {
        this.id = id;
    }

    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId());
    }
}
