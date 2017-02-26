import spock.lang.Specification

class SpockExample extends Specification{

    def "addition test "() {
        when: "2 is added to 2"
        int sum = 2 + 2

        then: "sum should be 4"
        sum == 4
    }

    def "Data Drive test - maximum of two numbers"() {
        expect:
        Math.max(a, b) == c

        where:
        a << [3, 5, 9]
        b << [7, 4, 9]
        c << [7, 5, 9]
    }

    def "Data Drive test - minimum of #a and #b is #c"() {
        expect:
        Math.min(a, b) == c

        where:
        a | b || c
        3 | 7 || 3
        5 | 4 || 4
        9 | 9 || 9
    }
}