package com.imhero.show.domain;

import com.imhero.config.BaseEntity;
import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "shows")
public class Show extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime showFromDate;
    private LocalDateTime showToDate;
    private String delYn;

    @OneToMany(mappedBy = "show")
    List<ShowDetail> showDetails = new ArrayList<>();

    private Show(String title, String artist, String place, User user, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        this.title = title;
        this.artist = artist;
        this.place = place;
        this.user = user;
        this.showFromDate = showFromDate;
        this.showToDate = showToDate;
        this.delYn = delYn;
    }

    public static Show of(String title, String artist, String place, User user, LocalDateTime showFromDate, LocalDateTime showToDate, String delYn) {
        return new Show(title, artist, place, user, showFromDate, showToDate, delYn);
    }

    public Show modify(String title, String artist, String place, User user, LocalDateTime showFromDate, LocalDateTime showToDate) {
        if (StringUtils.hasText(title)) {
            this.title = title;
        }
        if (StringUtils.hasText(artist)) {
            this.artist = artist;
        }
        if (StringUtils.hasText(place)) {
            this.place = place;
        }
        if (!ObjectUtils.isEmpty(user)) {
            this.user = user;
        }
        if (!ObjectUtils.isEmpty(showFromDate)) {
            this.showFromDate = showFromDate;
        }
        if (!ObjectUtils.isEmpty(showToDate)) {
            this.showToDate = showToDate;
        }
        return this;
    }

    public void cancel() {
        if (this.delYn.equals("Y")) {
            throw new ImheroApplicationException(ErrorCode.ALREADY_DELETED);
        }

        this.delYn = "Y";
    }
}
