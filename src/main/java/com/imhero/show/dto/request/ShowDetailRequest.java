package com.imhero.show.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetailRequest {

    private Long id;

    @JsonProperty("show_id")
    private Long showId;

    private Integer sequence;

    @JsonProperty("show_from_dt")
    private LocalDateTime showFromDt;

    @JsonProperty("show_to_dt")
    private LocalDateTime showToDt;

    @JsonProperty("reservation_from_dt")
    private LocalDateTime reservationFromDt;

    @JsonProperty("reservation_to_dt")
    private LocalDateTime reservationToDt;

    private String delYn;

}
