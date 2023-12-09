package com.imhero.user.service

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import com.imhero.user.domain.Role
import com.imhero.user.domain.User
import com.imhero.user.dto.UserDto
import com.imhero.user.dto.request.LoginRequest
import com.imhero.user.dto.request.UserRequest
import com.imhero.user.dto.response.LoginResponse
import com.imhero.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared

import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class UserServiceTest extends Specification {

    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder

    @Shared String email = "email@gamil.com"
    @Shared String password = "password1!!1"
    @Shared String passwordCheck = "password1!!1"
    @Shared String username = "username"

    def "회원 가입"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")
        UserRequest userRequest = new UserRequest(email, password,  username)

        when:
        UserDto userResult = userService.save(userRequest)

        then:
        userRepository.save(_) >> user
        userRepository.findUserByEmail(_) >> Optional.empty()
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 가입시 이미 가입된 회원인 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        userRepository.save(_) >> { throw new DataIntegrityViolationException("Unique constraint violation") }
        userService.save(userRequest)

        then:
        IllegalArgumentException e = thrown()
        e.getMessage() == "이미 가입된 회원입니다."
    }

    def "회원 가입시 에러가 발생한 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        userRepository.save(_) >> { throw new Exception("exception") }
        userService.save(userRequest)

        then:
        IllegalArgumentException e = thrown()
    }

    def "회원 조회"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")

        when:
        UserDto userResult = userService.findUserByEmail(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.of(user)
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getPassword() == passwordCheck
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 조회시 회원이 없는 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)

        when:
        userService.findUserByEmail(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }

    def "회원 아이디로 회원 조회"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")
        userRepository.findById(_) >> Optional.of(user)

        when:
        User findUser = userService.getUserByIdOrElseThrow(1L)

        then:
        findUser.getEmail() == email
        findUser.getPassword() == password
        findUser.getPassword() == passwordCheck
        findUser.getUsername() == username
        findUser.getRole() == Role.USER
        findUser.getDelYn() == "N"
    }

    def "회원 아이디로 회원 조회시 유저가 없는 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")
        userRepository.findById(_) >> Optional.empty()

        when:
        User findUser = userService.getUserByIdOrElseThrow(1L)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.USER_NOT_FOUND
    }

    def "회원 수정"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        UserDto userResult = userService.update(userRequest)

        then:
        userRepository.findUserByEmail(_) >> Optional.of(user)
        userResult.getEmail() == email
        userResult.getPassword() == password
        userResult.getUsername() == username
        userResult.getRole() == Role.USER
        userResult.getDelYn() == "N"
    }

    def "회원 수정시 회원이 없는 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        UserRequest userRequest = new UserRequest(email, password, username)

        when:
        userService.update(userRequest)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }

    def "회원 탈퇴"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")

        when:
        boolean result = userService.withdraw(email)

        then:
        result
        userRepository.findUserByEmail(_) >> Optional.of(user)
        user.getDelYn() == "Y"
    }

    def "회원 탈퇴시 이미 탈퇴된 회원인 경우" () {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "Y")

        when:
        boolean result = userService.withdraw(email)

        then:
        !result
        userRepository.findUserByEmail(_) >> Optional.of(user)
        user.getDelYn() == "Y"
    }

    def "회원 탈퇴시 회원이 없는 경우"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        User user = User.of(email, password, username, "N")

        when:
        userService.withdraw(email)

        then:
        userRepository.findUserByEmail(_) >> Optional.empty()
        IllegalArgumentException e = thrown()
        e.getMessage() == "회원이 없습니다."
    }

    def "로그인"() {
        given:
        UserRepository userRepository = Mock(UserRepository.class)
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder)
        LoginRequest loginRequest = new LoginRequest(email, password)
        User user = User.of(email, password, username, "N")
        userRepository.findUserByEmail(_) >> Optional.of(user)

        when:
        LoginResponse loginResponse = userService.login(loginRequest)

        then:
        loginResponse.getId() == user.getId()
    }
}
