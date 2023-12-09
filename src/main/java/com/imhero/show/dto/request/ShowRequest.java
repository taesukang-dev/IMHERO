package com.imhero.show.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShowRequest {
    private Long id;
    private String title;
    private String artist;
    private String place;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("show_from_date")
    private LocalDateTime showFromDate;
    @JsonProperty("show_to_date")
    private LocalDateTime showToDate;
    // TODO: 구현할 것
//    private List<ShowDetailRequest> = new ArrayList<>();
}
