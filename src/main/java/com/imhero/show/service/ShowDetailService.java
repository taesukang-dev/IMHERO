package com.imhero.show.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.show.domain.ShowDetail;
import com.imhero.show.dto.request.ShowDetailRequest;
import com.imhero.show.repository.ShowDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShowDetailService {

    private final ShowDetailRepository showDetailRepository;
    private final ShowService showService;

    public Long save(ShowDetailRequest showDetailRequest) {
        return showDetailRepository.save(
                    ShowDetail.of(
                            showService.getShowByIdOrElseThrow(showDetailRequest.getShowId()),
                            showDetailRequest.getSequence(),
                            showDetailRequest.getShowFromDt(),
                            showDetailRequest.getShowToDt(),
                            showDetailRequest.getReservationFromDt(),
                            showDetailRequest.getReservationToDt(),
                            showDetailRequest.getDelYn()
                    )).getId();
    }

    public void modify(ShowDetailRequest showDetailRequest) {
        getShowDetailByIdOrElseThrow(showDetailRequest.getId())
                .modify(showDetailRequest.getSequence(),
                        showDetailRequest.getShowFromDt(),
                        showDetailRequest.getShowToDt(),
                        showDetailRequest.getReservationFromDt(),
                        showDetailRequest.getReservationToDt(),
                        showDetailRequest.getDelYn());
    }

    public void delete(Long showDetailId) {
        getShowDetailByIdOrElseThrow(showDetailId).cancel();
    }

    @Transactional(readOnly = true)
    public ShowDetail getShowDetailByIdOrElseThrow(Long showDetailId) {
        return showDetailRepository
                .findById(showDetailId)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SHOW_DETAIL_NOT_FOUND));
    }

    ShowDetail getShowDetailByIdAndDelYn(Long id, String delYn) {
        return showDetailRepository
                .findShowDetailByIdAndDelYn(id, delYn)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SHOW_DETAIL_NOT_FOUND));
    }

}
