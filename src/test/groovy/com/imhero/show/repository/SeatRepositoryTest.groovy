package com.imhero.show.repository

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.domain.ShowDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager

@ActiveProfiles(profiles = "test")
@SpringBootTest
@Transactional
class SeatRepositoryTest extends Specification {

    @Autowired ShowDetailRepository showDetailRepository
    @Autowired SeatRepository seatRepository
    @Autowired EntityManager em

    ShowDetail showDetail

    def setup() {
        showDetail = new ShowDetail()
        showDetailRepository.save(showDetail)
    }

    def "좌석 등록"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        Seat savedSeat = seatRepository.save(seat)

        then:
        seat == savedSeat
    }

    def "좌석 단건 조회"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        Seat savedSeat = seatRepository.save(seat)
        Seat findSeat = seatRepository.findById(seat.getId()).get()

        then:
        savedSeat == findSeat
    }

    def "좌석 단건 조회 - x lock"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        Seat savedSeat = seatRepository.save(seat)
        Seat findSeat = seatRepository.findBySeatWithPessimisticLock(seat.getId()).get()

        then:
        savedSeat == findSeat
    }

    def "좌석 전체 조회"() {
        given:
        Seat seat1 = Seat.of(showDetail, Grade.A, 10)
        Seat seat2 = Seat.of(showDetail, Grade.R, 5)

        when:
        seatRepository.saveAll(List.of(seat1, seat2))

        then:
        seatRepository.findAll().size() == 2
    }

    def "좌석 수정"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        Seat savedSeat = seatRepository.save(seat)
        savedSeat.modify(Grade.VIP, 3)
        em.flush()
        Seat findSeat = seatRepository.findById(seat.getId()).get()

        then:
        findSeat.getShowDetail() == showDetail
        findSeat.getGradeDetails().getGrade() == Grade.VIP.getName()
        findSeat.getGradeDetails().getPrice() == Grade.VIP.getPrice()
        findSeat.getTotalQuantity() == 3
    }

    def "좌석 부분 수정"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        Seat savedSeat = seatRepository.save(seat)
        savedSeat.modify(null, -1)
        em.flush()
        Seat findSeat = seatRepository.findById(seat.getId()).get()

        then:
        findSeat.getShowDetail() == savedSeat.getShowDetail()
        findSeat.getGradeDetails() == savedSeat.getGradeDetails()
        findSeat.getTotalQuantity() == savedSeat.getTotalQuantity()
        findSeat.getCurrentQuantity() == savedSeat.getCurrentQuantity()
    }

    def "좌석 삭제"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        seatRepository.save(seat)
        seatRepository.delete(seat)

        then:
        seatRepository.findAll().isEmpty()
    }

    def "좌석 수량 변경(예매 시 수량 감소)"() {
        given:
        int total = 10
        int count = 3
        Seat seat = Seat.of(showDetail, Grade.A, total)

        when:
        seatRepository.save(seat)
        seat.reserve(count)

        then:
        seat.getCurrentQuantity() == total - count
    }

    def "예매 시 좌석이 부족하면 예외를 던진다"() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        seatRepository.save(seat)
        seat.reserve(11)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.INSUFFICIENT_SEAT
    }

    def "좌석 수량 변경(예매 취소 시 수량 증가)"() {
        given:
        int total = 10
        int reserveCount = 7
        int cancelCount = 3
        Seat seat = Seat.of(showDetail, Grade.A, total)

        when:
        seatRepository.save(seat)
        seat.reserve(reserveCount)
        seat.cancel(cancelCount)

        then:
        seat.currentQuantity == total - reserveCount + cancelCount
    }

    def "예매 취소 시 전체 수량이 현재+취소 수량을 초과하면 예외를 던진다."() {
        given:
        Seat seat = Seat.of(showDetail, Grade.A, 10)

        when:
        seatRepository.save(seat)
        seat.cancel(3)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.EXCEEDED_SEAT_CANCELLATION
    }
}
