package com.imhero.user.dto;

import com.imhero.user.domain.Role;
import com.imhero.user.domain.User;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String username;
    private Role role;
    private String delYn;

    public static UserDto from(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getRole(),
                user.getDelYn());
    }
}
