package com.imhero.reservation.dto.response;

import com.imhero.reservation.dto.ReservationSellerDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSellerResponse {
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

    public static ReservationSellerResponse of(ReservationSellerDto reservationSellerDto) {
        return new ReservationSellerResponse(
                reservationSellerDto.getUserId(),
                reservationSellerDto.getEmail(),
                reservationSellerDto.getUsername(),
                reservationSellerDto.getShowId(),
                reservationSellerDto.getArtist(),
                reservationSellerDto.getPlace(),
                reservationSellerDto.getTitle(),
                reservationSellerDto.getRegistrant(),
                reservationSellerDto.getShowFromDate(),
                reservationSellerDto.getShowToDate(),
                reservationSellerDto.getShowDelYn(),
                reservationSellerDto.getShowDetailId(),
                reservationSellerDto.getReservationFromDt(),
                reservationSellerDto.getReservationToDt(),
                reservationSellerDto.getSequence(),
                reservationSellerDto.getShowDetailDelYn(),
                reservationSellerDto.getSeatId(),
                reservationSellerDto.getGrade(),
                reservationSellerDto.getPrice(),
                reservationSellerDto.getCurrentQuantity(),
                reservationSellerDto.getTotalQuantity());
    }
}
