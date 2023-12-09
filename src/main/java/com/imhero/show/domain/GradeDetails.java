package com.imhero.show.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GradeDetails {

    private String grade;
    private int price;

    public GradeDetails(Grade grade) {
        this.grade = grade.getName();
        this.price = grade.getPrice();
    }

}
