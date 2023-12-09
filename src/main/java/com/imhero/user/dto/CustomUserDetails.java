package com.imhero.user.dto;

import com.imhero.user.domain.User;

import java.util.ArrayList;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private User user;

    public CustomUserDetails(User user) {
        super(user.getEmail(), user.getPassword(), new ArrayList<>());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
