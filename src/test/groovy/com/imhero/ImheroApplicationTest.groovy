package com.imhero

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.transaction.Transactional

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ImheroApplicationTest extends Specification{

    def "contextLoads"() {
        expect:
        1==1
    }
}
