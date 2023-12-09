package com.imhero.show.domain

import com.imhero.config.exception.ErrorCode
import com.imhero.config.exception.ImheroApplicationException
import spock.lang.Specification

class GradeTest extends Specification {

    def "문자열로 입력받은 등급을 enum으로 변환한다"() {
        expect:
        Grade.from(name) == expectedGrade

        where:
        name    | expectedGrade
        "A"     | Grade.A
        "R"     | Grade.R
        "VIP"   | Grade.VIP
    }

    def "입력한 등급이 enum에 존재하지 않으면 예외를 던진다"() {
        given:
        String input = "Wrong"

        when:
        Grade.from(input)

        then:
        def e = thrown(ImheroApplicationException)
        e.errorCode == ErrorCode.GRADE_NOT_FOUND
    }
}
