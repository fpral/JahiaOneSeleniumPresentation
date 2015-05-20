package org.jahia.selenium.modules.elasticsearch;

import org.jahia.selenium.ModuleTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

/**
 * Test class for the elastic search module. T execute these tests, you need to have imported the site
 * resources/sites/searchsite_demo.zip in Digital Factory.
 * <p>
 * PermissionTest: Verify that users can only search for content they have reading permission on
 * <p>
 * VisibilityTest: Verify that the search takes the visibility conditions of the content into account
 *
 */
public class ElasticSearchTests extends ModuleTest {

    private static final String JCR_USER_PASSWORD = "password";
    private static final String RICHTEXT = "Selenium demonstration at Jahia One";
    private static final String PERMISSION_PAGE = "Permissions";
    private static final String RICHTEXT_VISIBLE = "Digital Factory includes Elastic Search!";
    private static final String RICHTEXT_NOT_VISIBLE = "Text visible on Sundays only";
    private static final String VISIBILITY_PAGE = "Visibility";

    @Test
    public void PermissionTest(){

        login("jcr1", JCR_USER_PASSWORD);
        openSiteHomePage();
        search(RICHTEXT, PERMISSION_PAGE, true);
        logout();

        login("jcr2", JCR_USER_PASSWORD);
        openSiteHomePage();
        search(RICHTEXT, PERMISSION_PAGE, false);
        search(PERMISSION_PAGE, PERMISSION_PAGE, true);
        logout();

        login("jcr3", JCR_USER_PASSWORD);
        openSiteHomePage();
        search(RICHTEXT,PERMISSION_PAGE,false);
        search(PERMISSION_PAGE, PERMISSION_PAGE, false);
        logout();

        openSiteHomePage();
        search(RICHTEXT,PERMISSION_PAGE, false);
        search(PERMISSION_PAGE,PERMISSION_PAGE,false);
    }

    @Test
    public void VisibilityTest(){
        openSiteHomePage();
        search(RICHTEXT_VISIBLE, VISIBILITY_PAGE, true);
        search(RICHTEXT_NOT_VISIBLE, VISIBILITY_PAGE, false);
    }

    private void openSiteHomePage(){
        openUrl(baseUrl + "/sites/" + getPropertyValue("elasticsearchtest.sitekey", "searchsite") + "/home.html");
    }

    /**
     * Search for a String then verify that the result is displayed or not
     * @param textToSearch String searched
     * @param linkPage Page corresponding to the search result
     * @param searchSuccessful
     */
    private void search(String textToSearch, String linkPage, boolean searchSuccessful){
        type(By.id("searchTerm"), textToSearch);
        driver.findElement(By.id("searchTerm")).sendKeys(Keys.RETURN);

        if(searchSuccessful){
            verifyResultDisplayed(linkPage,textToSearch);
        }
        else{
            verifyResultNotDisplayed(linkPage,textToSearch);
        }
    }

    private void verifyResultDisplayed(String pageLink, String textToFind){
        verifyLink(By.linkText(pageLink),
                getPropertyValue("elasticsearchtest.sitekey", "searchsite") + "/home/" +
                        pageLink.toLowerCase() + ".html");
        verifyTextPresent(textToFind);
    }

    private void verifyResultNotDisplayed(String pageLink, String textToFind){
        verifyElementNotDisplayed(By.linkText(pageLink));
        verifyTextNotPresent(textToFind);
    }

}
