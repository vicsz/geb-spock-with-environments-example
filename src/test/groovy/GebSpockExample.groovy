import geb.spock.GebReportingSpec
import groovy.json.JsonSlurper
import org.openqa.selenium.Cookie

class GebSpockExample extends GebReportingSpec{

    def "Base example"() {
        given: "I browse to wikipedia"
        go "http://www.wikipedia.org"

        expect: "Title should be Wikipedia"
        title == "Wikipedia"
    }

    def "API example (non-Html content) -- health check"() {
        given:
        go "https://status.github.com/api/status.json"

        expect:
        //Get contents of pre , WebDriver seems to wrap content in some HTML tags
        $('pre').text().startsWith('{"status":"good"')
    }

    def "JSON parsing -- health check"() {
        given:
        go "https://status.github.com/api/status.json"

        expect:
        new JsonSlurper().parseText($('pre').text()).status == 'good'
    }

    def "Cookie example"() {

        when:
        go "https://www.wikipedia.org/"
        driver.manage().addCookie(new Cookie("my-geb-cookie", "foobar", ".wikipedia.org", "/", null))
        go "https://www.wikipedia.org/"

        then:
        title == "Wikipedia"
        driver.manage().getCookieNamed("my-geb-cookie").value == "foobar"
    }

    def "Query parameter string example"() {
        given:
        //equivalent of : https://en.wikipedia.org/wiki/Special:Search?search=Toronto
        go 'https://en.wikipedia.org/wiki/Special:Search', search:'Toronto'

        expect:
        title == 'Toronto - Wikipedia'
    }

}
