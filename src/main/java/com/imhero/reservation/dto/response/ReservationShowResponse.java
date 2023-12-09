package com.imhero.reservation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationShowResponse {
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

    private int count;

    public ReservationShowResponse(Long userId, String email, String username, Long showId, String artist, String place, String title, String registrant, LocalDateTime showFromDate, LocalDateTime showToDate, String showDelYn, Long showDetailId, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, Integer sequence, String showDetailDelYn, Long seatId, String grade, Integer price, Integer currentQuantity, Integer totalQuantity, Long reservationId, String reservationDelYn, Long reservationSeatId) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.showId = showId;
        this.artist = artist;
        this.place = place;
        this.title = title;
        this.registrant = registrant;
        this.showFromDate = showFromDate;
        this.showToDate = showToDate;
        this.showDelYn = showDelYn;
        this.showDetailId = showDetailId;
        this.reservationFromDt = reservationFromDt;
        this.reservationToDt = reservationToDt;
        this.sequence = sequence;
        this.showDetailDelYn = showDetailDelYn;
        this.seatId = seatId;
        this.grade = grade;
        this.price = price;
        this.currentQuantity = currentQuantity;
        this.totalQuantity = totalQuantity;
        this.reservationId = reservationId;
        this.reservationDelYn = reservationDelYn;
        this.reservationSeatId = reservationSeatId;
    }

    public static ReservationShowResponse of(Long userId, String email, String username, Long showId, String artist, String place, String title, String registrant, LocalDateTime showFromDate, LocalDateTime showToDate, String showDelYn, Long showDetailId, LocalDateTime reservationFromDt, LocalDateTime reservationToDt, Integer sequence, String showDetailDelYn, Long seatId, String grade, Integer price, Integer currentQuantity, Integer totalQuantity, Long reservationId, String reservationDelYn, Long reservationSeatId) {
        return new ReservationShowResponse(userId, email, username, showId, artist, place, title, registrant, showFromDate, showToDate, showDelYn, showDetailId, reservationFromDt, reservationToDt, sequence, showDetailDelYn, seatId, grade, price, currentQuantity, totalQuantity, reservationId, reservationDelYn, reservationSeatId);
    }
}
