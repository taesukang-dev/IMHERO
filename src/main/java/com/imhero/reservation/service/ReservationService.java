package com.imhero.reservation.service;

import com.imhero.config.aop.DistributedLock;
import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.reservation.domain.Reservation;
import com.imhero.reservation.dto.request.ReservationCancelRequest;
import com.imhero.reservation.dto.request.ReservationRequest;
import com.imhero.reservation.dto.response.ReservationResponse;
import com.imhero.reservation.dto.response.ReservationSellerResponse;
import com.imhero.reservation.repository.ReservationRepository;
import com.imhero.show.domain.Seat;
import com.imhero.show.service.SeatService;
import com.imhero.user.components.AuthenticatedUser;
import com.imhero.user.domain.User;
import com.imhero.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final SeatService seatService;
    private final AuthenticatedUser authenticatedUser;

    @Transactional(readOnly = true)
    public List<ReservationSellerResponse> findAllSeatByEmail(String email) {
        return reservationRepository.findAllSeatByEmail(email)
                .stream().map(ReservationSellerResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponse findAllReservationByEmail(String email) {
        return ReservationResponse
                .of(email, reservationRepository.findAllReservationByEmail(email));
    }

    @DistributedLock
    public Set<Long> save(ReservationRequest reservationRequest) {
        User user = getUser(authenticatedUser.getUser().getId());
        Seat seat = seatService.getSeatByIdOrElseThrow(reservationRequest.getSeatId());
        seat.reserve(reservationRequest.getCount());

        List<Reservation> reservations = reservationRepository.saveAll(
                IntStream.range(0, reservationRequest.getCount())
                        .mapToObj((i) -> Reservation.of(user, seat, "N"))
                        .collect(Collectors.toList()));

        return reservations.stream()
                .mapToLong(Reservation::getId)
                .boxed()
                .collect(Collectors.toSet());
    }

    @DistributedLock
    public void cancel(ReservationCancelRequest reservationCancelRequest) {
        User user = getUser(authenticatedUser.getUser().getId());
        Seat seat = seatService.getSeatByIdOrElseThrow(reservationCancelRequest.getSeatId());

        List<Reservation> reservations = reservationRepository.findAllById(reservationCancelRequest.getIds());
        if (!reservations.get(0).getUser().getEmail().equals(user.getEmail())) {
            throw new ImheroApplicationException(ErrorCode.UNAUTHORIZED_BEHAVIOR);
        }

        Set<Long> reservationIds = reservations.stream()
                .map(Reservation::getId)
                .collect(Collectors.toSet());

        int deleteCount = reservationRepository.updateDelYnByIds(reservationIds);
        seat.cancel(deleteCount);
    }

    private User getUser(Long userId) {
        return userService.getUserByIdOrElseThrow(userId);
    }
}
