package com.imhero.show.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.show.dto.request.ShowDetailRequest
import com.imhero.show.repository.ShowDetailRepository
import com.imhero.user.domain.User
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles(profiles = "test")
@SpringBootTest
@Transactional
class ShowDetailServiceTest extends Specification {

    @Shared
    LocalDateTime now = LocalDateTime.now()

    def "공연 회차 추가"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowService showService = Mock(ShowService.class)
        ShowDetail showDetail = Mock(ShowDetail.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, showService)
        showService.getShowByIdOrElseThrow(_) >> getShow()
        showDetailRepository.save(_) >> showDetail
        showDetail.getId() >> 1L

        when:
        Long savedId = showDetailService.save(showDetailRequest)

        then:
        savedId == 1L
    }

    def "공연 회차 수정"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, Mock(ShowService.class))
        showDetailRepository.findById(_) >> Optional.of(showDetail)

        when:
        showDetailService.modify(showDetailRequest)

        then:
        showDetailRequest.getSequence() == showDetail.getSequence()
    }

    def "공연 회차 수정 시 공연 미존재"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, Mock(ShowService.class))
        showDetailRepository.findById(_) >> Optional.empty()

        when:
        showDetailService.modify(showDetailRequest)

        then:
        def e = thrown(ImheroApplicationException.class)
        e.errorCode == ErrorCode.SHOW_DETAIL_NOT_FOUND
    }

    def "공연 회차 취소"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, Mock(ShowService.class))
        ShowDetail showDetail = getShowDetail()
        showDetailRepository.findById(_) >> Optional.of(showDetail)

        when:
        showDetailService.delete(showDetail.getId())

        then:
        showDetail.getDelYn() == "Y"
    }

    def "공연 회차 조회"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, Mock(ShowService.class))
        ShowDetail showDetail = getShowDetail()
        showDetailRepository.findShowDetailByIdAndDelYn(_, _) >> Optional.of(showDetail)

        when:
        ShowDetail findShowDetail = showDetailService.getShowDetailByIdAndDelYn(showDetail.getId(), "N")

        then:
        findShowDetail.getSequence() == getShowDetail().getSequence()
    }

    def "공연 회차 조회 시 공연 회차 미존재"() {
        given:
        ShowDetailRepository showDetailRepository = Mock(ShowDetailRepository.class)
        ShowDetailService showDetailService = getShowDetailService(showDetailRepository, Mock(ShowService.class))
        ShowDetail showDetail = getShowDetail()
        showDetailRepository.findShowDetailByIdAndDelYn(_, _) >> Optional.empty()

        when:
        showDetailService.getShowDetailByIdAndDelYn(showDetail.getId(), "N")

        then:
        def e = thrown(ImheroApplicationException.class)
        e.errorCode == ErrorCode.SHOW_DETAIL_NOT_FOUND
    }

    private getShowDetailService(ShowDetailRepository showDetailRepository, ShowService showService) {
        return new ShowDetailService(showDetailRepository, showService)
    }

    private getShowDetailRequest() {
        LocalDateTime now = LocalDateTime.now()
        return new ShowDetailRequest(1L, 1L, 1, now, now, now, now, "N")
    }

    private getShowDetail() {
        return ShowDetail.of(show, 1, now, now, now, now, "N")
    }

    private getShow() {
        return Show.of("title", "artist", "place", user, now, now, "N")
    }

    private getUser() {
        return User.of("email", "pw", "name", "N")
    }
}
