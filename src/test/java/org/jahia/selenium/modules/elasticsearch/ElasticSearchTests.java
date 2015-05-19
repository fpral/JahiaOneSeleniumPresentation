package org.jahia.selenium.modules.elasticsearch;

import org.jahia.selenium.ModuleTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Francois on 5/19/2015.
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
        search(RICHTEXT,PERMISSION_PAGE,false);
        search(PERMISSION_PAGE, PERMISSION_PAGE, true);
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
        openUrl(baseUrl + "/sites/" + getPropertyValue("elasticsearchtest.sitename", "searchsite") + "/home.html");
    }

    private void search(String textToSearch, String pageLink, boolean searchSuccessful){
        type(By.id("searchTerm"), textToSearch);
        driver.findElement(By.id("searchTerm")).sendKeys(Keys.RETURN);

        if(searchSuccessful){
            verifyResultDisplayed(pageLink,textToSearch);
        }
        else{
            verifyResultNotDisplayed(pageLink,textToSearch);
        }
    }

    private void verifyResultDisplayed(String pageLink, String textToFind){
        verifyLink(By.linkText(pageLink),
                getPropertyValue("elasticsearchtest.sitename", "searchsite") + "/home/" +
                        pageLink.toLowerCase() + ".html");
        verifytextPresent(textToFind);
    }

    private void verifyResultNotDisplayed(String pageLink, String textToFind){
        verifyElementNotDisplayed(By.linkText(pageLink), "The link" + pageLink + "has been found but was not expected");
        verifyTextNotPresent(textToFind);
    }

}
