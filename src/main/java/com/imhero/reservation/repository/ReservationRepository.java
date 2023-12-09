package com.imhero.reservation.repository;

import com.imhero.reservation.domain.Reservation;
import com.imhero.reservation.dto.ReservationDto;
import com.imhero.reservation.dto.ReservationSellerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    @Query("SELECT s FROM Reservation s where s.id IN :ids")
    List<Reservation> findAllById(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.delYn = 'Y' WHERE r.id IN :ids AND r.delYn = 'N'")
    int updateDelYnByIds(@Param("ids") Set<Long> ids);

    @Query("SELECT" +
            " new com.imhero.reservation.dto.ReservationDto(" +
            " r.user.id, r.user.email, r.user.username," +
            " r.seat.showDetail.show.id, r.seat.showDetail.show.artist, r.seat.showDetail.show.place," +
            " r.seat.showDetail.show.title, r.seat.showDetail.show.createdBy, " +
            " r.seat.showDetail.show.showFromDate, r.seat.showDetail.show.showToDate, r.seat.showDetail.show.delYn," +
            " r.seat.showDetail.id, r.seat.showDetail.reservationFromDt, r.seat.showDetail.reservationToDt," +
            " r.seat.showDetail.sequence, r.seat.showDetail.delYn," +
            " r.seat.id, r.seat.gradeDetails.grade, r.seat.gradeDetails.price, r.seat.currentQuantity, r.seat.totalQuantity," +
            " r.id, r.delYn, r.seat.id" +
            ")" +
            " FROM Reservation r" +
            " JOIN r.seat" +
            " JOIN r.user" +
            " JOIN r.seat.showDetail" +
            " JOIN r.seat.showDetail.show" +
            " where r.user.email = :email")
    List<ReservationDto> findAllReservationByEmail(@Param("email") String email);

    @Query("SELECT" +
            " new com.imhero.reservation.dto.ReservationSellerDto(" +
            " s.showDetail.show.user.id, s.showDetail.show.user.email, s.showDetail.show.user.username," +
            " s.showDetail.show.id, s.showDetail.show.artist, s.showDetail.show.place," +
            " s.showDetail.show.title, s.showDetail.show.createdBy, " +
            " s.showDetail.show.showFromDate, s.showDetail.show.showToDate, s.showDetail.show.delYn," +
            " s.showDetail.id, s.showDetail.reservationFromDt, s.showDetail.reservationToDt," +
            " s.showDetail.sequence, s.showDetail.delYn," +
            " s.id, s.gradeDetails.grade, s.gradeDetails.price, s.currentQuantity, s.totalQuantity" +
            ")" +
            " FROM Seat s" +
            " JOIN s.showDetail" +
            " JOIN s.showDetail.show" +
            " JOIN s.showDetail.show.user" +
            " where s.showDetail.show.user.email = :email")
    List<ReservationSellerDto> findAllSeatByEmail(@Param("email") String email);
}
