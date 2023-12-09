package com.imhero.show.repository

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import java.time.LocalDateTime

@ActiveProfiles(profiles = "test")
@SpringBootTest
@Transactional
class ShowDetailRepositoryTest extends Specification {

    @Autowired
    private ShowRepository showRepository
    @Autowired
    private ShowDetailRepository showDetailRepository
    @Autowired
    private UserRepository userRepository
    @Autowired
    private EntityManager em;

    Show show
    LocalDateTime now = LocalDateTime.now()

    def setup() {
        User user = User.of("email", "password", "username", "N")
        show = Show.of("title", "artist", "place", user, now, now, "N")

        userRepository.save(user)
        showRepository.save(show)
    }

    def "회차별 공연 단건 조회"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "N")

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)
        ShowDetail findShowDetail = showDetailRepository.findById(showDetail.getId()).get()

        then:
        savedShowDetail == findShowDetail
    }

    def "회차별 공연 전체 조회"() {
        given:
        ShowDetail showDetail1 = ShowDetail.of(show, 1, now, now, now, now, "N")
        ShowDetail showDetail2 = ShowDetail.of(show, 2, now, now, now, now, "N")

        when:
        showDetailRepository.saveAll(List.of(showDetail1, showDetail2))

        then:
        showDetailRepository.findAll().size() == 2
    }

    def "세부 공연 등록"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "N")

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)

        then:
        showDetail == savedShowDetail
    }

    def "세부 공연 수정"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "N")
        LocalDateTime modifiedTime = LocalDateTime.now().plusDays(1L)

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)
        savedShowDetail.modify(2, modifiedTime, modifiedTime, modifiedTime, modifiedTime, "Y")
        em.flush()
        ShowDetail findShowDetail = showDetailRepository.findById(showDetail.getId()).get()

        then:
        findShowDetail.getShow() == savedShowDetail.getShow()
        findShowDetail.getSequence() == 2
        findShowDetail.getShowFromDt() == modifiedTime
        findShowDetail.getShowToDt() == modifiedTime
        findShowDetail.getReservationFromDt() == modifiedTime
        findShowDetail.getReservationToDt() == modifiedTime
        findShowDetail.getDelYn() == "Y"
    }

    def "세부 공연 부분 수정"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "N")

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)
        savedShowDetail.modify(null, null, null, null, null, null)
        em.flush()
        ShowDetail findShowDetail = showDetailRepository.findById(showDetail.getId()).get()

        then:
        findShowDetail.getShow() == savedShowDetail.getShow()
        findShowDetail.getSequence() == savedShowDetail.getSequence()
        findShowDetail.getShowFromDt() == savedShowDetail.getShowFromDt()
        findShowDetail.getShowToDt() == savedShowDetail.getShowToDt()
        findShowDetail.getReservationFromDt() == savedShowDetail.getReservationFromDt()
        findShowDetail.getReservationToDt() == savedShowDetail.getReservationToDt()
        findShowDetail.getDelYn() == savedShowDetail.getDelYn()
    }

    def "세부 공연 취소"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "N")

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)
        savedShowDetail.cancel()
        em.flush()
        ShowDetail findShowDetail = showDetailRepository.findById(showDetail.getId()).get()

        then:
        findShowDetail.getDelYn() == "Y"
    }

    def "세부 공연이 이미 취소되어 있는 경우"() {
        given:
        ShowDetail showDetail = ShowDetail.of(show, 1, now, now, now, now, "Y")

        when:
        ShowDetail savedShowDetail = showDetailRepository.save(showDetail)
        savedShowDetail.cancel()
        em.flush()
        showDetailRepository.findById(showDetail.getId()).get()

        then:
        def e = thrown(ImheroApplicationException.class)
        e.errorCode == ErrorCode.ALREADY_DELETED
    }
}