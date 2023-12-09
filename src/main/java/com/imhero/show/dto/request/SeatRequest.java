package com.imhero.show.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequest {

    private Long id;
    private Long showDetailId;
    private String grade;
    private int price;
    private int totalQuantity;
    private int currentQuantity;
}
