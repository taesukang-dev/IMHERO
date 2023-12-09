package com.imhero.show.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.show.domain.Show
import com.imhero.show.dto.request.ShowRequest
import com.imhero.show.dto.response.ShowResponse
import com.imhero.show.repository.ShowRepository
import com.imhero.user.domain.User
import com.imhero.user.service.UserService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ShowServiceTest extends Specification {

    def "공연 조회"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        ShowService showService = getShowService(showRepository, Mock(UserService.class))
        Show show = getShow()
        List<Show> showList = Arrays.asList(show, show, show, show, show, show);
        Page<Show> showPage = new PageImpl<>(showList, PageRequest.of(0, 1), showList.size());
        showRepository.findAllByDelYn(_, _) >> showPage

        when:
        Page<ShowResponse> findShowList = showService.findAll(PageRequest.of(0, 1), "N")

        then:
        findShowList.size() == 6
        findShowList.totalPages == 6
        findShowList.getContent().get(0).title == show.getTitle()
    }

    def "공연 조회 - fulltext"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        ShowService showService = getShowService(showRepository, Mock(UserService.class))
        Show show = getShow()
        List<Show> showList = Arrays.asList(show, show, show, show, show, show);
        Page<Show> showPage = new PageImpl<>(showList, PageRequest.of(0, 1), showList.size());
        showRepository.findAllByFullTextSearch(_, _, _, _) >> showPage

        when:
        Page<ShowResponse> findShowList = showService.findAllByFullTextSearch(PageRequest.of(0, 1), "test", "", "")

        then:
        findShowList.size() == 6
        findShowList.totalPages == 6
        findShowList.getContent().get(0).title == show.getTitle()
    }

    def "공연 조회 개별"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        Show show = getShow()
        ShowService showService = getShowService(showRepository, Mock(UserService.class))
        showRepository.findShowByIdAndDelYn(_, _) >> Optional.of(show)

        when:
        ShowResponse findShow = showService.findById(1L, "N")

        then:
        findShow.getTitle() == show.getTitle()
        findShow.getArtist() == show.getArtist()
        findShow.getPlace() == show.getPlace()
        findShow.getShowFromDate() == show.getShowFromDate()
        findShow.getShowToDate() == show.getShowToDate()
    }


    def "공연 조회 개별 시 공연이 없는 경우"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        Show show = getShow()
        ShowService showService = getShowService(showRepository, Mock(UserService.class))
        showRepository.findShowByIdAndDelYn(_, _) >> Optional.empty()

        when:
        Show findShow = showService.findById(1L, "N")

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.SHOW_NOT_FOUND
    }

    def "공연 생성"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        UserService userService = Mock(UserService.class)
        ShowService showService = getShowService(showRepository, userService)
        Show show = Mock(Show.class)
        ShowRequest showRequest = getShowRequest()
        showRepository.save(_) >> show

        when:
        show.getId() >> 1L
        Long id = showService.save(showRequest)

        then:
        id == 1L
    }

    def "공연 수정"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        UserService userService = Mock(UserService.class)
        ShowService showService = getShowService(showRepository, userService)
        LocalDateTime given = LocalDateTime.now()
        ShowRequest showRequest = new ShowRequest(1L, "modified", "modified", "modified", 1L, given, given)
        Show show = getShow()
        showRepository.findById(_) >> Optional.of(show)

        when:
        showService.modify(showRequest)

        then:
        showRequest.getTitle() == show.getTitle()
        showRequest.getArtist() == show.getArtist()
        showRequest.getShowFromDate() == show.getShowFromDate()
        showRequest.getShowToDate() == show.getShowToDate()
    }

    def "공연 수정 시 공연 없는 경우"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        UserService userService = Mock(UserService.class)
        ShowService showService = getShowService(showRepository, userService)
        LocalDateTime given = LocalDateTime.now()
        ShowRequest showRequest = new ShowRequest(1L, "modified", "modified", "modified", 1L, given, given)
        showRepository.findById(_) >> Optional.empty()

        when:
        showService.modify(showRequest)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.SHOW_NOT_FOUND
    }

    def "공연 삭제"() {
        given:
        ShowRepository showRepository = Mock(ShowRepository.class)
        UserService userService = Mock(UserService.class)
        ShowService showService = getShowService(showRepository, userService)
        Show show = getShow()
        showRepository.findById(_) >> Optional.of(show)

        when:
        showService.delete(show.getId())

        then:
        show.getDelYn() == "Y"
    }

    private ShowService getShowService(ShowRepository showRepository, UserService userService) {
        return new ShowService(showRepository, userService)
    }

    private ShowRequest getShowRequest() {
        return new ShowRequest(null, "title", "artist", "place", 1L, LocalDateTime.now(), LocalDateTime.now())
    }

    private Show getShow() {
        return Show.of("title", "artist", "place", getUser(), LocalDateTime.now(), LocalDateTime.now(), "N")
    }

    private User getUser() {
        return User.of("email@gmail.com", "password", "username", "N")
    }
}
