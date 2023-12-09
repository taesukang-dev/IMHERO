package com.imhero.show.dto.response;

import com.imhero.show.domain.ShowDetail;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetailResponse {
    private Long id;
    private Integer sequence;
    private LocalDateTime showFromDt;
    private LocalDateTime showToDt;
    private LocalDateTime reservationFromDt;
    private LocalDateTime reservationToDt;

    public static ShowDetailResponse from(ShowDetail showDetail) {
        return new ShowDetailResponse(showDetail.getId(),
                showDetail.getSequence(),
                showDetail.getShowFromDt(),
                showDetail.getShowToDt(),
                showDetail.getReservationFromDt(),
                showDetail.getReservationToDt());
    }
}
