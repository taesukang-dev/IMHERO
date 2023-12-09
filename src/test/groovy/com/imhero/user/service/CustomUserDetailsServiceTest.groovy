package com.imhero.user.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.user.domain.User
import com.imhero.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import spock.lang.Shared
import spock.lang.Specification

class CustomUserDetailsServiceTest extends Specification {

    @Shared String email = "email@gamil.com"
    @Shared String password = "password1!!1"
    @Shared String username = "username"

    def "이메일로 회원 조회"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserDetailsService userDetailsService = new CustomUserDetailsService(userRepository)
        User user = User.of(email, password, username, "N")
        userRepository.findUserByEmail(_) >> Optional.of(user)

        when:
        UserDetails userDetails = userDetailsService.loadUserByUsername(email)

        then:
        userDetails.getUsername() == email
    }

    def "이메일로 회원 조회 실패"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserDetailsService userDetailsService = new CustomUserDetailsService(userRepository)
        userRepository.findUserByEmail(_) >> Optional.empty()

        when:
        userDetailsService.loadUserByUsername(email)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.EMAIL_NOT_FOUND
    }
}
