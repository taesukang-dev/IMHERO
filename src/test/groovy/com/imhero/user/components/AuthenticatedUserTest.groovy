package com.imhero.user.components

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.fixture.Fixture
import com.imhero.user.domain.User
import com.imhero.user.dto.UserDto
import com.imhero.user.dto.request.UserRequest
import com.imhero.user.repository.UserRepository
import com.imhero.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@SpringBootTest
@Transactional
class AuthenticatedUserTest extends Specification {

    @Autowired AuthenticationManager authenticationManager
    @Autowired UserService userService
    @Autowired UserRepository userRepository

    def "로그인 유저 정보 조회"() {
        given:
        //회원가입
        User user = Fixture.getUser()
        UserDto userDto = userService.save(new UserRequest(user.getEmail(), user.getPassword(), user.getUsername()))

        //로그인
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication)

        AuthenticatedUser authUser = new AuthenticatedUser()

        when:
        User findUser = userRepository.findById(userDto.getId()).get()
        User authenticatedUser = authUser.getUser()

        then:
        authenticatedUser == findUser
    }

    def "비로그인 유저 정보 조회 시 예외 처리"() {
        given:
        SecurityContextHolder.getContext().setAuthentication(null)
        AuthenticatedUser authUser = new AuthenticatedUser()

        when:
        authUser.getUser()

        then:
        def e = thrown(ImheroApplicationException.class)
        e.getErrorCode() == ErrorCode.UNAUTHORIZED_BEHAVIOR
    }
}