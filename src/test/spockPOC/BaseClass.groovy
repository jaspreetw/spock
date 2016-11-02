package spockPOC

/**
 * Created by jaspreetwalia on 01/11/2016.
 */
class BaseClass {
    Properties props
    File propsFile
    def headers= ["Content-Type": "application/json"]

    def "readPropertyFile"(String key) {
        props = new Properties()
        propsFile = new File('src/test/resources/properties/config.properties')
        props.load ( propsFile.newDataInputStream ( ) )
        def value =  props.getProperty (key)
        return value
    }
}
