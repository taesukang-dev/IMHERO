package com.imhero.reservation.dto.response;

import com.imhero.reservation.dto.ReservationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private String email;
    private List<ReservationShowResponse> shows;

    public static ReservationResponse of(String email, List<ReservationDto> reservationDtos) {
        return new ReservationResponse(email, new ArrayList<>(reservationDtos.stream()
                .collect(Collectors.groupingBy(
                        ReservationDto::getReservationSeatId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                dtoList -> {
                                    ReservationDto reservationDto = dtoList.get(0);
                                    ReservationShowResponse seatResponse = getSeatResponse(reservationDto);
                                    seatResponse.setCount(dtoList.size());
                                    return seatResponse;
                                }
                        )
                ))
                .values()));
    }

    public static ReservationShowResponse getSeatResponse(ReservationDto reservationDto) {
        return ReservationShowResponse.of(
                reservationDto.getUserId(),
                reservationDto.getEmail(),
                reservationDto.getUsername(),
                reservationDto.getShowId(),
                reservationDto.getArtist(),
                reservationDto.getPlace(),
                reservationDto.getTitle(),
                reservationDto.getRegistrant(),
                reservationDto.getShowFromDate(),
                reservationDto.getShowToDate(),
                reservationDto.getShowDelYn(),
                reservationDto.getShowDetailId(),
                reservationDto.getReservationFromDt(),
                reservationDto.getReservationToDt(),
                reservationDto.getSequence(),
                reservationDto.getShowDetailDelYn(),
                reservationDto.getSeatId(),
                reservationDto.getGrade(),
                reservationDto.getPrice(),
                reservationDto.getCurrentQuantity(),
                reservationDto.getTotalQuantity(),
                reservationDto.getReservationId(),
                reservationDto.getReservationDelYn(),
                reservationDto.getReservationSeatId());
    }
}
