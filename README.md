# JahiaOneSeleniumPresentation
This module is an example of how you can develop a test for a simple module using selenium.

## Prerequisites
This module has been tested with FireFox 33.1.1 and Chrome 42

You need to import the site searchsite_demo.zip in your Digital Factory 7.1 server. You need the template set sample-bootstrap-template. The site comes with 3 users: jcr1, jcr2 and jcr3, all with the password: password
The site has the following structure:
```
Home
 |- Permissions (page visible only by users jcr1 and jcr2)
        |- Bootstrap rich text ("Selenium demonstration at Jahia One!"): content only visible by the user jcr1
 |- Visibility
        |- Bootstrap rich text ("Digital Factory includes Elastic Search!") only visible from Monday to Friday
        |- Bootstrap rich text ("Text visible on Sundays only") only visible on Sundays
```
## Configuration
In the pom.xml, you will find the two profiles "selenium" and "ElasticSearchTests". The selenium profile allows you to specify the host server/port/context of your Digital Factory and the "ElasticSearchTests" is just here to specify the sitekey of the site used in the tests. It is more a way to show how you can retrieve properties in your tests.
 
## Execution
Simply run the maven command:

`
mvn -Pselenium,ElasticSearchTests,selenium-tests-execution -DxmlTest=ElasticSearchTestSuite.xml clean verify 
`
