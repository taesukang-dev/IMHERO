package com.imhero.show.dto.response;

import com.imhero.show.domain.Show;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShowResponse {
    private Long id;
    private String title;
    private String artist;
    private String place;
    private Long userId;
    private LocalDateTime showFromDate;
    private LocalDateTime showToDate;
    private List<ShowDetailResponse> showDetailResponses;

    public static ShowResponse from(Show show) {
        return new ShowResponse(
                show.getId(),
                show.getTitle(),
                show.getArtist(),
                show.getPlace(),
                show.getUser().getId(),
                show.getShowFromDate(),
                show.getShowToDate(),
                show.getShowDetails()
                        .stream()
                        .map(ShowDetailResponse::from)
                        .collect(Collectors.toList()));
    }
}
