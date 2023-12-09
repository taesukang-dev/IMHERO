package com.imhero.reservation.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.fixture.Fixture
import com.imhero.reservation.domain.Reservation
import com.imhero.reservation.dto.ReservationDto
import com.imhero.reservation.dto.ReservationSellerDto
import com.imhero.reservation.dto.request.ReservationCancelRequest
import com.imhero.reservation.dto.request.ReservationRequest
import com.imhero.reservation.dto.response.ReservationResponse
import com.imhero.reservation.dto.response.ReservationSellerResponse
import com.imhero.reservation.repository.ReservationRepository
import com.imhero.show.domain.Grade
import com.imhero.show.domain.Seat
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.show.repository.SeatRepository
import com.imhero.show.repository.ShowDetailRepository
import com.imhero.show.repository.ShowRepository
import com.imhero.show.service.SeatService
import com.imhero.user.components.AuthenticatedUser
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import com.imhero.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ReservationServiceTest extends Specification {
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

    def "예매 생성"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        AuthenticatedUser authenticatedUser = getAuthenticatedUser()
        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService, authenticatedUser)

        User user = Fixture.getUser()
        userService.getUserByIdOrElseThrow(_) >> user
        authenticatedUser.getUser() >> user
        seatService.getSeatByIdOrElseThrow(_) >> Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))

        ReservationRequest reservationRequest = getReservationRequest()

        Reservation reservation1 = Mock(Reservation.class)
        reservation1.getId() >> 1L
        Reservation reservation2 = Mock(Reservation.class)
        reservation2.getId() >> 2L

        reservationRepository.saveAll(_) >> List.of(reservation1, reservation2)

        when:
        def ids = reservationService.save(reservationRequest)

        then:
        ids.size() == 2
    }

    def "예매 취소"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        AuthenticatedUser authenticatedUser = getAuthenticatedUser()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService, authenticatedUser)
        Reservation reservation = Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))
        Seat seat = Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))

        int count = 3
        int before = seat.totalQuantity - seat.reserve(count)

        reservation.getDelYn()
        userService.getUserByIdOrElseThrow(_) >> reservation.getUser()
        seatService.getSeatByIdOrElseThrow(_) >> seat
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))), Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))]
        reservationRepository.updateDelYnByIds(_) >> count
        authenticatedUser.getUser() >> reservation.getUser()

        when:
        def reservationCancelRequest = getReservationCancelRequest()
        reservationService.cancel(reservationCancelRequest)

        then:
        seat.getCurrentQuantity() == before + reservationCancelRequest.getIds().size()
    }

    def "예매 취소시 유저가 같지 않은 경우"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        AuthenticatedUser authenticatedUser = getAuthenticatedUser()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService, authenticatedUser)
        Reservation reservation = Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))

        userService.getUserByIdOrElseThrow(_) >> Fixture.getNewUser()
        Seat seat = Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))
        seat.reserve(3)
        seatService.getSeatByIdOrElseThrow(_) >> seat
        authenticatedUser.getUser() >> Fixture.getUser()
        reservationRepository.findAllById(_) >> [reservation, Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser())))), Fixture.getReservation(Fixture.getUser(), Fixture.getSeat(Fixture.getShowDetail(Fixture.getShow(Fixture.getUser()))))]

        when:
        def reservationCancelRequest = getReservationCancelRequest()
        reservationService.cancel(reservationCancelRequest)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.UNAUTHORIZED_BEHAVIOR
    }

    def "회원 이메일로 모든 예약 조회"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        AuthenticatedUser authenticatedUser = getAuthenticatedUser()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService, authenticatedUser)

        when:
        User user = Fixture.getUser()
        Show show = Fixture.getShow(user)
        ShowDetail showDetail = Fixture.getShowDetail(show)
        Seat seat = Fixture.getSeat(showDetail)
        Reservation reservation = Fixture.getReservation(user, seat)

        Seat seat2 = Seat.of(showDetail, Grade.VIP, 100)
        Reservation reservation2 = Fixture.getReservation(user, seat2)

        userRepository.save(user)
        showRepository.save(show)
        showDetailRepository.save(showDetail)
        seatRepository.save(seat)
        reservationRepository.save(reservation)

        seatRepository.save(seat2)
        reservationRepository.save(reservation2)

        ReservationDto reservationDto = Fixture.getReservationDto(6L)
        ReservationDto reservationDto2 = Fixture.getReservationDto(7L)

        reservationRepository.findAllReservationByEmail(_) >> List.of(reservationDto, reservationDto2)
        ReservationResponse reservationResponse = reservationService.findAllReservationByEmail("test")

        then:
        reservationResponse.shows.size() == 2
    }

    def "판매자 이메일로 모든 seat 조회"() {
        given:
        UserService userService = getUserService()
        SeatService seatService = getSeatService()
        ReservationRepository reservationRepository = getReservationRepository()
        AuthenticatedUser authenticatedUser = getAuthenticatedUser()

        ReservationService reservationService = new ReservationService(reservationRepository, userService, seatService, authenticatedUser)

        when:
        User user = Fixture.getUser()
        Show show = Fixture.getShow(user)
        ShowDetail showDetail = Fixture.getShowDetail(show)
        Seat seat = Fixture.getSeat(showDetail)

        Seat seat2 = Seat.of(showDetail, Grade.VIP, 100)

        userRepository.save(user)
        showRepository.save(show)
        showDetailRepository.save(showDetail)
        seatRepository.save(seat)

        seatRepository.save(seat2)

        ReservationSellerDto reservationDto = Fixture.getReservationSellerDto()
        ReservationSellerDto reservationDto2 = Fixture.getReservationSellerDto()
        reservationRepository.findAllSeatByEmail(_) >> List.of(reservationDto, reservationDto2)
        List<ReservationSellerResponse> sellers = reservationService.findAllSeatByEmail("test")

        then:
        sellers.size() == 2
    }

    private ReservationCancelRequest getReservationCancelRequest() {
        return new ReservationCancelRequest(1L, Set.of(1L, 2L, 3L))
    }

    private getSeatService() {
        return Mock(SeatService.class)
    }

    private getUserService() {
        return Mock(UserService.class)
    }

    private getReservationRepository() {
        return Mock(ReservationRepository.class)
    }

    private getReservationRequest() {
        def given = LocalDateTime.now()
        return new ReservationRequest(1L, 2, given)
    }

    private getAuthenticatedUser() {
        return Mock(AuthenticatedUser.class)
    }
}