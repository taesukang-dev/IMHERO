package com.imhero.reservation.domain;

import com.imhero.config.BaseTimeEntity;
import com.imhero.show.domain.Seat;
import com.imhero.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seat seat;

    private String delYn;

    private Reservation(User user, Seat seat, String delYn) {
        this.user = user;
        this.seat = seat;
        this.delYn = delYn;
    }

    public static Reservation of(User user, Seat seat, String delYn) {
        return new Reservation(user, seat, delYn);
    }
}
