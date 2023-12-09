package com.imhero.user.domain;

import lombok.Getter;

public enum Role {

    USER("user"),
    ADMIN("admin");

    @Getter
    private String name;

    Role(String name) {
        this.name = name;
    }
}
