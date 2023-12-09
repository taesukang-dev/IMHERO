package com.imhero.show.controller;

import com.imhero.config.exception.Response;
import com.imhero.show.dto.request.ShowDetailRequest;
import com.imhero.show.service.ShowDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/show/detail")
@RequiredArgsConstructor
public class ShowDetailController {

    private final ShowDetailService showDetailService;

    @PostMapping("")
    public Response<Long> save(@RequestBody ShowDetailRequest showDetailRequest) {
        return Response.success(showDetailService.save(showDetailRequest));
    }

    @PutMapping("")
    public Response<Void> modify(@RequestBody ShowDetailRequest showDetailRequest) {
        showDetailService.modify(showDetailRequest);
        return Response.success();
    }

    @DeleteMapping("{showDetailId}")
    public Response<Void> delete(@PathVariable Long showDetailId) {
        showDetailService.delete(showDetailId);
        return Response.success();
    }

}
