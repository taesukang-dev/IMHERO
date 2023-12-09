package com.imhero.reservation.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long seatId;
    private Integer count;
    private LocalDateTime requestTime;
}
