package com.imhero.reservation.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long userId;
    private String email;
    private String username;
    private Long showId;
    private String artist;
    private String place;
    private String title;
    private String registrant;
    private LocalDateTime showFromDate;
    private LocalDateTime showToDate;
    private String showDelYn;
    private Long showDetailId;
    private LocalDateTime reservationFromDt;
    private LocalDateTime reservationToDt;
    private Integer sequence;
    private String showDetailDelYn;
    private Long seatId;
    private String grade;
    private Integer price;
    private Integer currentQuantity;
    private Integer totalQuantity;
    private Long reservationId;
    private String reservationDelYn;
    private Long reservationSeatId;
}
