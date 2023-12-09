package com.imhero.reservation.controller;

import com.imhero.config.exception.Response;
import com.imhero.reservation.dto.request.ReservationCancelRequest;
import com.imhero.reservation.dto.request.ReservationRequest;
import com.imhero.reservation.dto.response.ReservationResponse;
import com.imhero.reservation.dto.response.ReservationSellerResponse;
import com.imhero.reservation.service.ReservationService;
import com.imhero.user.components.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/api/v1/show/reservation")
@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticatedUser authenticatedUser;

    @GetMapping("/seller")
    public Response<List<ReservationSellerResponse>> findAllSeatByEmail() {
        return Response.success(reservationService.findAllSeatByEmail(authenticatedUser.getUser().getEmail()));
    }

    @GetMapping("")
    public Response<ReservationResponse> findAllReservationByEmail() {
        return Response.success(reservationService.findAllReservationByEmail(authenticatedUser.getUser().getEmail()));
    }

    @PostMapping("")
    public Response<Set<Long>> save(@RequestBody ReservationRequest reservationRequest) {
        return Response.success(reservationService.save(reservationRequest));
    }

    @DeleteMapping("")
    public Response<Void> cancel(@RequestBody ReservationCancelRequest reservationCancelRequest) {
        reservationService.cancel(reservationCancelRequest);
        return Response.success();
    }
}
