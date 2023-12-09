package com.imhero.show.controller;

import com.imhero.config.exception.Response;
import com.imhero.show.dto.request.SeatRequest;
import com.imhero.show.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/seat")
@RestController
public class SeatController {
    private final SeatService seatService;

    @PostMapping("")
    public Response<Long> save(@RequestBody SeatRequest seatRequest) {
        return Response.success(seatService.save(seatRequest));
    }

    @PutMapping("")
    public Response<Void> modify(@RequestBody SeatRequest seatRequest) {
        seatService.modify(seatRequest);
        return Response.success();
    }
}
