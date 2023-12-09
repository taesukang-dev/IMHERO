package com.imhero.show.controller;

import com.imhero.config.exception.Response;
import com.imhero.show.dto.request.ShowRequest;
import com.imhero.show.dto.response.ShowResponse;
import com.imhero.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/show")
@RestController
public class ShowController {

    private final ShowService showService;

    @GetMapping("")
    public Response<Page<ShowResponse>> findAll(Pageable pageable, @RequestParam("del_yn") String delYn) {
        return Response.success(showService.findAll(pageable, delYn));
    }

    @GetMapping("/search")
    public Response<Page<ShowResponse>> findAllByFullTextSearch(
            Pageable pageable,
            @RequestParam("keyword") String keyword,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate
    ) {
        return Response.success(showService.findAllByFullTextSearch(pageable, keyword, fromDate, toDate));
    }

    @GetMapping("/{showId}")
    public Response<ShowResponse> findById(@PathVariable Long showId, @RequestParam("del_yn") String delYn) {
        return Response.success(showService.findById(showId, delYn));
    }

    @PostMapping("")
    public Response<Long> save(@RequestBody ShowRequest showRequest) {
        return Response.success(showService.save(showRequest));
    }

    @PutMapping("")
    public Response<Void> modify(@RequestBody ShowRequest showRequest) {
        showService.modify(showRequest);
        return Response.success();
    }

    @DeleteMapping("/{showId}")
    public Response<Void> delete(@PathVariable Long showId) {
        showService.delete(showId);
        return Response.success();
    }
}
