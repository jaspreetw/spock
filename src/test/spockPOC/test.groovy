package spockPOC

import groovyx.net.http.RESTClient
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Title

@Stepwise
@Title('These tests will test documenting features of Spock Framework')
@Narrative('''
As a Developer using Spock Framework for testing,
I want to be see Specifications documented,
so that my Business Analyst understand what is this all about.
''')
class SpockPOCTest extends Specification {

    BaseClass baseClass = new BaseClass()
    def devurl = baseClass.readPropertyFile('devUrl')
    def clearCacheURl = baseClass.readPropertyFile('clearcache')
    def costestimateUrl = baseClass.readPropertyFile('costEstimate')

    def "clearCacheTest"() {
        def restClient = new RESTClient(devurl)

        when: "When ClearCache API is called"
        def response = restClient.get(path:clearCacheURl)
        def jsonResponse = response.getData()

        then: "Cache should be cleared and  " +
                "response status should be 200 , status should be success and data should be true"
        assert response.status == 200
        assert jsonResponse.status == "success"
        assert jsonResponse.data instanceof ArrayList
        assert jsonResponse.data.find { it ==  true}
    }


    def "createCostEstimateWithInvalidJSON"() {
        def restClient = new RESTClient(devurl)

        restClient.handler.failure = restClient.handler.success

        given: "JSON File with invalid attributes"
        String fileContents = new File('src/test/resources/json/invalidCENotNullServiceId.json').text

        when: "Post Request is called"
        def response = restClient.post(
                path:costestimateUrl, headers:baseClass.headers, body:fileContents){it}

        def json = response.getData()
        println '****************'
        println json.url
        def t = response.getContentType()

        then: "Response status should be 500, status should be failure"
        assert response.status == 500
        assert json.status == "failure"
    }
}

