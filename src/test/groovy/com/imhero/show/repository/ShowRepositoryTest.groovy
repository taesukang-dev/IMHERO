package com.imhero.show.repository

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Show
import com.imhero.show.domain.ShowDetail
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import java.time.LocalDateTime

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ShowRepositoryTest extends Specification {

    @Autowired private ShowRepository showRepository
    @Autowired private ShowDetailRepository showDetailRepository
    @Autowired private UserRepository userRepository
    @Autowired private EntityManager em;

    User user
    User modifyUser

    def setup() {
        user = User.of("email", "password", "useranme", "N")
        modifyUser = User.of("modify", "modify", "modify", "N")

        userRepository.save(user)
        userRepository.save(modifyUser)
    }

    def "공연 단건 조회"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        Show show = Show.of("title", "artist", "place", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        ShowDetail showDetail1 = ShowDetail.of(show, 1, now, now, now, now, "N")
        ShowDetail showDetail2 = ShowDetail.of(show, 2, now, now, now, now, "N")

        when:
        Show savedShow = showRepository.save(show)
        showDetailRepository.save(showDetail1)
        showDetailRepository.save(showDetail2)
        em.flush()
        Show findShow = showRepository.findById(savedShow.getId()).get()

        then:
        show == findShow
        findShow.showDetails.size() == 2
    }

    def "공연 전체 조회"() {
        given:
        Show show1 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show2 = Show.of("title2", "artist2", "place2", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        showRepository.saveAll(List.of(show1, show2))

        then:
        showRepository.findAll().size() == 2
    }

    def "공연 등록"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        savedShow == findShow
    }

    def "공연 수정"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        LocalDateTime modifiedTime = LocalDateTime.now()

        when:
        Show savedShow = showRepository.save(show)
        show.modify("modify", "modify", "modify", modifyUser, modifiedTime, modifiedTime)
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        findShow.getTitle() == "modify"
        findShow.getArtist() == "modify"
        findShow.getPlace() == "modify"
        findShow.getUser() == modifyUser
        findShow.getShowFromDate() == modifiedTime
        findShow.getShowToDate() == modifiedTime
    }

    def "공연 부분 수정"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        LocalDateTime modifiedTime = LocalDateTime.now()

        when:
        Show savedShow = showRepository.save(show)
        show.modify(null, null, null, null, null, null)
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        findShow.getTitle() == savedShow.getTitle()
        findShow.getArtist() == savedShow.getArtist()
        findShow.getPlace() == savedShow.getPlace()
        findShow.getUser() == savedShow.getUser()
        findShow.getShowFromDate() == savedShow.getShowFromDate()
        findShow.getShowToDate() == savedShow.getShowToDate()
    }

    def "공연 취소"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)
        savedShow.cancel()
        em.flush()
        Show findShow = showRepository.findById(show.getId()).get()

        then:
        findShow.getDelYn() == "Y"
    }

    def "공연이 이미 취소되어 있는 경우"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "Y")

        when:
        Show savedShow = showRepository.save(show)
        savedShow.cancel()

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.ALREADY_DELETED
    }

    def "취소되지 않은 공연 아이디로 개별 조회"() {
        given:
        Show show = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        Show savedShow = showRepository.save(show)
        Show findShow = showRepository.findShowByIdAndDelYn(savedShow.getId(), "N").get()

        then:
        findShow.getTitle() == savedShow.getTitle()
        findShow.getArtist() == savedShow.getArtist()
        findShow.getPlace() == savedShow.getPlace()
        findShow.getUser() == savedShow.getUser()
        findShow.getShowFromDate() == savedShow.getShowFromDate()
        findShow.getShowToDate() == savedShow.getShowToDate()
    }

    def "취소되지 않은 모든 공연 조회"() {
        given:
        Show show1 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show2 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show3 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show4 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")
        Show show5 = Show.of("title1", "artist1", "place1", user, LocalDateTime.now(), LocalDateTime.now(), "N")

        when:
        showRepository.saveAll(List.of(show1, show2, show3, show4, show5))
        Page<Show> shows = showRepository.findAllByDelYn(PageRequest.of(0, 2), "N")

        then:
        shows.totalElements == 5
        shows.totalPages == 3
    }
}
