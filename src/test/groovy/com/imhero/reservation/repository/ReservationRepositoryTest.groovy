package com.imhero.reservation.repository


import com.imhero.fixture.Fixture
import com.imhero.reservation.domain.Reservation
import com.imhero.reservation.dto.ReservationDto
import com.imhero.reservation.dto.ReservationSellerDto
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.show.repository.SeatRepository
import com.imhero.show.repository.ShowDetailRepository
import com.imhero.show.repository.ShowRepository
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ReservationRepositoryTest extends Specification {

    @Autowired
    private ReservationRepository reservationRepository
    @Autowired
    private UserRepository userRepository
    @Autowired
    private SeatRepository seatRepository

    @Autowired
    private ShowRepository showRepository
    @Autowired
    private ShowDetailRepository showDetailRepository

    def "예약 생성"() {
        given:
        Reservation reservation = Reservation.of(getUser(), seat, "N")

        when:
        Reservation savedReservation = reservationRepository.save(reservation)

        then:
        reservation == savedReservation
    }

    def "예약 단건 조회"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation = Reservation.of(user, seat, "N")

        when:
        reservationRepository.save(reservation)
        Reservation findReservation = reservationRepository.findById(reservation.getId()).get()

        then:
        reservation == findReservation
        user == findReservation.getUser()
        seat == findReservation.getSeat()
    }

    def "없는 예약 단건 조회시"() {
        when:
        reservationRepository.findById(99L).get()

        then:
        NoSuchElementException e = thrown()
    }

    def "예약 전체 조회"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "N")

        when:
        reservationRepository.saveAll(List.of(reservation1, reservation2))
        List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId())

        then:
        reservations.size() == 2
    }

    def "예약 취소"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "N")

        when:
        Reservation savedReservation1 = reservationRepository.save(reservation1)
        Reservation savedReservation2 = reservationRepository.save(reservation2)
        int deletedCount = reservationRepository.updateDelYnByIds(Set.of(savedReservation1.getId(), savedReservation2.getId()))

        then:
        deletedCount == 2
    }

    def "예약이 이미 취소된 경우"() {
        given:
        User user = getUser()
        Seat seat = getSeat()
        Reservation reservation1 = Reservation.of(user, seat, "N")
        Reservation reservation2 = Reservation.of(user, seat, "Y")

        when:
        Reservation savedReservation1 = reservationRepository.save(reservation1)
        Reservation savedReservation2 = reservationRepository.save(reservation2)
        int deletedCount = reservationRepository.updateDelYnByIds(Set.of(savedReservation1.getId(), savedReservation2.getId()))

        then:
        deletedCount == 1
    }

    def "회원 계정으로 모든 예약 조회"() {
        given:
        User user = Fixture.getUser()
        Show show = Fixture.getShow(user)
        ShowDetail showDetail = Fixture.getShowDetail(show)
        Seat seat = Fixture.getSeat(showDetail)
        Reservation reservation = Fixture.getReservation(user, seat)
        userRepository.save(user)
        showRepository.save(show)
        showDetailRepository.save(showDetail)
        seatRepository.save(seat)
        reservationRepository.save(reservation)


        Show show2 = Show.of("title2", "artist2", "place2", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        ShowDetail showDetail2 = Fixture.getShowDetail(show2)
        Seat seat2 = Fixture.getSeat(showDetail2)
        Reservation reservation2 = Fixture.getReservation(user, seat2)

        showRepository.save(show2)
        showDetailRepository.save(showDetail2)
        seatRepository.save(seat2)
        reservationRepository.save(reservation2)

        when:
        List<ReservationDto> reservations = reservationRepository.findAllReservationByEmail(user.getEmail())

        then:
        reservations.size() == 2
    }

    def "판매자 계정으로 모든 예약 조회"() {
        given:
        User user = Fixture.getUser()
        Show show = Fixture.getShow(user)
        ShowDetail showDetail = Fixture.getShowDetail(show)
        Seat seat = Fixture.getSeat(showDetail)
        userRepository.save(user)
        showRepository.save(show)
        showDetailRepository.save(showDetail)
        seatRepository.save(seat)


        Show show2 = Show.of("title2", "artist2", "place2", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        ShowDetail showDetail2 = Fixture.getShowDetail(show2)
        Seat seat2 = Fixture.getSeat(showDetail2)

        showRepository.save(show2)
        showDetailRepository.save(showDetail2)
        seatRepository.save(seat2)

        when:
        List<ReservationSellerDto> reservations = reservationRepository.findAllSeatByEmail(user.getEmail())

        then:
        reservations.size() == 2
    }


    private Seat getSeat() {
        return seatRepository.save(Seat.of(null, Grade.A, 30))
    }

    private User getUser() {
        return userRepository.save(User.of("test@gmail.com", "password", "test", "N"))
    }
}