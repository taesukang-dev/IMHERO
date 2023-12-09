package com.imhero.show.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.domain.ShowDetail
import com.imhero.show.dto.request.SeatRequest
import com.imhero.show.repository.SeatRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@SpringBootTest
@Transactional
class SeatServiceTest extends Specification {

    def "좌석 단건 조회"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findById(_) >> Optional.of(seat)

        when:
        Seat findSeat = seatService.getSeatByIdOrElseThrow(seat.getId())

        then:
        findSeat == seat
    }

    def "좌석 단건 조회 시 좌석 미존재"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findById(_) >> Optional.empty()

        when:
        seatService.getSeatByIdOrElseThrow(seat.getId())

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.SEAT_NOT_FOUND
    }

    def "좌석 단건 조회 - x lock"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findBySeatWithPessimisticLock(_) >> Optional.of(seat)

        when:
        Seat findSeat = seatService.getSeatWithPessimisticLockOrElseThrow(seat.getId())

        then:
        findSeat == seat
    }

    def "좌석 단건 조회 시 좌석 미존재 - x lock"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findBySeatWithPessimisticLock(_) >> Optional.empty()

        when:
        seatService.getSeatWithPessimisticLockOrElseThrow(seat.getId())

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.SEAT_NOT_FOUND
    }

    def "좌석 생성"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = Mock(Seat.class)
        seatRepository.save(_) >> seat
        seat.getId() >> 1L

        when:
        Long savedId = seatService.save(seatRequest)

        then:
        savedId == 1L
    }

    def "좌석 수정"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findById(_) >> Optional.of(seat)

        when:
        seatService.modify(seatRequest)
        Seat findSeat = seatService.getSeatByIdOrElseThrow(seat.getId())

        then:
        findSeat.getGradeDetails().getGrade() == "VIP"
    }

    def "좌석 예약"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findById(_) >> Optional.of(seat)

        when:
        int reservedCount = seatService.reserve(seat.getId(), 5)

        then:
        reservedCount == 5
    }

    def "좌석 취소"() {
        given:
        SeatRepository seatRepository = Mock(SeatRepository.class)
        SeatService seatService = getSeatService(seatRepository, Mock(ShowDetailService.class))
        Seat seat = getSeat()
        seatRepository.findById(_) >> Optional.of(seat)

        int count = 3
        seat.currentQuantity -= count

        when:
        int cancelCount = seatService.cancel(seat.getId(), count)

        then:
        cancelCount == count
    }

    private getSeatService(SeatRepository seatRepository, ShowDetailService showDetailService) {
        return new SeatService(seatRepository, showDetailService)
    }

    private getSeat() {
        return Seat.of(Mock(ShowDetail.class), Grade.A, 10)
    }

    private getSeatRequest() {
        return new SeatRequest(1L, 1L, "VIP", 0, 10, 0)
    }
}