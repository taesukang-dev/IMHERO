package com.imhero.fixture;

import com.imhero.reservation.domain.Reservation;
import com.imhero.reservation.dto.ReservationDto;
import com.imhero.reservation.dto.ReservationSellerDto;
import com.imhero.show.domain.Grade;
import com.imhero.show.domain.Seat;
import com.imhero.show.domain.Show;
import com.imhero.show.domain.ShowDetail;
import com.imhero.user.domain.User;

import java.time.LocalDateTime;

public class Fixture {

    public static User getUser() {
        return User.of("test@gmail.com", "12345678", "test", "N");
    }

    public static User getNewUser() {
        return User.of("newTest@gmail.com", "12345678", "newTest", "N");
    }

    public static Show getShow(User user) {
        return Show.of("title", "artist", "place", user, LocalDateTime.now(), LocalDateTime.now(), "N");
    }

    public static ShowDetail getShowDetail(Show show) {
        LocalDateTime now = LocalDateTime.now();
        return ShowDetail.of(show, 1, now, now, now, now, "N");
    }

    public static Seat getSeat(ShowDetail showDetail) {
        return Seat.of(showDetail, Grade.A, 30);
    }

    public static Reservation getReservation(User user, Seat seat) {
        return Reservation.of(user, seat, "N");
    }

    public static ReservationDto getReservationDto(long reservationSeatId) {
        LocalDateTime now = LocalDateTime.now();
        return new ReservationDto(
                1L,
                "email@gmail.com",
                "username",
                2L,
                "artist",
                "place",
                "title",
                "regi",
                now,
                now,
                "N",
                3L,
                now,
                now,
                3,
                "N",
                4L,
                "VIP",
                20000,
                100,
                100,
                5L,
                "N",
                reservationSeatId
        );
    }

    public static ReservationSellerDto getReservationSellerDto() {
        LocalDateTime now = LocalDateTime.now();
        return new ReservationSellerDto(
                1L,
                "email@gmail.com",
                "username",
                2L,
                "artist",
                "place",
                "title",
                "regi",
                now,
                now,
                "N",
                3L,
                now,
                now,
                3,
                "N",
                4L,
                "VIP",
                20000,
                100,
                100
        );
    }
}
