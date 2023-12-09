package com.imhero.show.domain;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import lombok.Getter;

@Getter
public enum Grade {

    A("A", 99_000),
    R("R", 149_000),
    VIP("VIP", 199_000);

    private String name;
    private int price;

    Grade(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Grade from(String name) {
        switch (name) {
            case "A": return Grade.A;
            case "R": return Grade.R;
            case "VIP": return Grade.VIP;
            default: throw new ImheroApplicationException(ErrorCode.GRADE_NOT_FOUND);
        }
    }
}