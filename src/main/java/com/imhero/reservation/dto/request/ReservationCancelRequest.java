package com.imhero.reservation.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCancelRequest {
    private Long seatId;
    Set<Long> ids = new HashSet<>();
}
