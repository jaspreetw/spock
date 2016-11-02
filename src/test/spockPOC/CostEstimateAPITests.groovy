package spockPOC

import groovyx.net.http.RESTClient
import spock.lang.Specification

/**
 * Created by jaspreetwalia on 01/11/2016.
 */
class CostEstimateAPITests extends Specification {

    def "createCostEstimationTest"() {
        RESTClient restClient = new RESTClient('https://devcostestimate.moveguides.com:18084')

        when: "costEstimation API is called"
        def response = restClient.get(path:'/api/v2/configuration/clear-cache')
        def jsonResponse = response.getData()

        then: "response status should be 200 , status should be success and data should be true"
        assert response.status == 200
        assert jsonResponse.status == "success"
        assert jsonResponse.data instanceof ArrayList
        assert jsonResponse.data.find { it ==  true}
    }


}
