package spockPOC

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

/**
 * Created by jaspreetwalia on 01/11/2016.
 */
@Stepwise

class RatesAPI extends Specification{
    BaseClass baseClass = new BaseClass()

    def devurl = baseClass.readPropertyFile('devUrl')
    def costestimateUrl = baseClass.readPropertyFile('costEstimate')
    static  requestId = null

    def "createCostEstimationForBlackRock"() {
        // https://devcostestimate.moveguides.com:18084/api/v2/cost-estimates/
        when: "Post Request is called"
        def http = new HTTPBuilder(devurl)
        http.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }

        http.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }
        String fileContents = new File('src/test/resources/json/blackrockInput.json').text

        def map = http.request(Method.POST, ContentType.JSON
        ) { req ->
            body = fileContents
            uri.path = costestimateUrl
            contentType = ContentType.JSON
        }

        def response = map['response']
        def reader = map['reader']

        if(reader.keySet().contains('data')) {
            requestId = reader.data.requestId.get(0)
            requestId = requestId.replace('[', "")
            requestId = requestId.replace(']', "")
        }
        println requestId
        then:
        assert response.status == 202
        assert reader.status == "success"
        assert reader.keySet().contains('data') ==  true
    }


    def "displayCostEstimationForBlackRock"() {
        def conditions = new PollingConditions(timeout:240, initialDelay: 2, factor: 4)

        given:
        def http = new HTTPBuilder(devurl)
        http.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }

        http.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when:
        def map = http.get(path:costestimateUrl+ "/" + requestId )

        def response = map['response']
        def reader = map['reader']
         println reader.data.get(0).creationRequestStatus

        then:
        conditions.eventually {
            println      reader.data.get(0).creationRequestStatus
            assert reader.data.get(0).creationRequestStatus == 'SUCCESS'
        }
    }
}
