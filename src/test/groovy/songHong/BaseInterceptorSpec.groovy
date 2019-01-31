package songHong


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(BaseInterceptor)
class BaseInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test base interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"base")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
