package com.imhero.show.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.show.domain.Grade;
import com.imhero.show.domain.Seat;
import com.imhero.show.dto.request.SeatRequest;
import com.imhero.show.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowDetailService showDetailService;

    @Transactional
    public Long save(SeatRequest seatRequest) {
        return seatRepository.save(
                Seat.of(
                        showDetailService.getShowDetailByIdOrElseThrow(seatRequest.getShowDetailId()),
                        Grade.from(seatRequest.getGrade()),
                        seatRequest.getTotalQuantity()
                )).getId();
    }

    @Transactional
    public void modify(SeatRequest seatRequest) {
        getSeatByIdOrElseThrow(seatRequest.getId())
                .modify(Grade.from(seatRequest.getGrade()),
                        seatRequest.getTotalQuantity());
    }

    int reserve(Long seatId, int count) {
        return getSeatByIdOrElseThrow(seatId).reserve(count);
    }

    int cancel(Long seatId, int count) {
        return getSeatByIdOrElseThrow(seatId).cancel(count);
    }

    public Seat getSeatByIdOrElseThrow(Long seatId) {
        return seatRepository
                .findById(seatId)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SEAT_NOT_FOUND));
    }

    public Seat getSeatWithPessimisticLockOrElseThrow(Long seatId) {
        return seatRepository
                .findBySeatWithPessimisticLock(seatId)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SEAT_NOT_FOUND));
    }
}
