package com.imhero.user.repository

import com.imhero.user.domain.Role
import com.imhero.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class UserRepositoryTest extends Specification {

    @Autowired private UserRepository userRepository;
    @Autowired EntityManager em;


    def "회원 가입"() {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)

        then:
        user == savedUser
    }

    def "회원 이메일 중복"() {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")
        User user2 = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        userRepository.save(user)
        userRepository.save(user2)

        then:
        DataIntegrityViolationException e = thrown()
    }

    def "회원 조회" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        User findUser = userRepository.findById(user.getId()).get()

        then:
        savedUser == findUser
    }

    def "회원 수정" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.modify("modify@gmail.com", "modify", "modifyUsername", Role.ADMIN)
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getEmail() == "modify@gmail.com"
        findUser.getPassword() == "modify"
        findUser.getUsername() == "modifyUsername"
        findUser.getRole() == Role.ADMIN
        findUser.getRole().getName() == Role.ADMIN.getName()
    }

    def "회원 부분 수정" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.modify(null, null, null, null)
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getEmail() == savedUser.getEmail()
        findUser.getPassword() == savedUser.getPassword()
        findUser.getUsername() == savedUser.getUsername()
        findUser.getDelYn() == savedUser.getDelYn()
        findUser.getRole() == savedUser.getRole()
    }

    def "회원 탈퇴" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "N")

        when:
        User savedUser = userRepository.save(user)
        savedUser.withdraw()
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        findUser.getDelYn() == "Y"
    }

    def "회원이 이미 탈퇴된 경우" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "Y")
        User savedUser = userRepository.save(user)

        when:
        boolean result = savedUser.withdraw()
        em.flush()
        User findUser = userRepository.findById(savedUser.getId()).get()

        then:
        !result
        findUser.getDelYn() == "Y"
    }

    def "회원을 email로 찾는 경우" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "Y")
        User savedUser = userRepository.save(user)

        when:
        User findUser = userRepository.findUserByEmail("test@gmail.com").get()

        then:
        findUser.getUsername() == savedUser.getUsername()
        findUser.getEmail() == savedUser.getEmail()
        findUser.getPassword() == savedUser.getPassword()
        findUser.getUsername() == savedUser.getUsername()
        findUser.getRole() == savedUser.getRole()
    }

    def "회원을 email 로 찾을 때 없는 경우" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "Y")
        userRepository.save(user)

        when:
        userRepository.findUserByEmail("None").get()

        then:
        NoSuchElementException e = thrown()
    }

    def "회원이 username 으로 찾는 경우" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "Y")
        User savedUser = userRepository.save(user)

        when:
        User findUser = userRepository.findUserByUsername("test").get()

        then:
        findUser.getUsername() == savedUser.getUsername()

        findUser.getEmail() == savedUser.getEmail()
        findUser.getPassword() == savedUser.getPassword()
        findUser.getUsername() == savedUser.getUsername()
        findUser.getRole() == savedUser.getRole()
    }

    def "회원을 usernaem 으로 찾을 때 없는 경우" () {
        given:
        User user = User.of("test@gmail.com", "12345678", "test", "Y")
        userRepository.save(user)

        when:
        userRepository.findUserByUsername("None").get()

        then:
        NoSuchElementException e = thrown()
    }
}
