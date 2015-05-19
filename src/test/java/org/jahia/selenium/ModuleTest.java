package org.jahia.selenium;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Francois on 5/19/2015.
 */
public class ModuleTest {
    private static final int SLEEP_TIME = 2000; //in seconds
    private static final long WEB_DRIVER_TIMEOUT = 20; //in seconds
    protected WebDriver driver;
    protected String baseUrl;

    public ModuleTest() {
        this.baseUrl = new StringBuilder("http://").append(getPropertyValue("selenium.jahia.host", "localhost")).append(":").append(getPropertyValue("selenium.jahia.port", "8080")).append(getPropertyValue("selenium.jahia.context", "")).toString();

    }

    @BeforeTest
    public void setUpTest(){
        if (driver == null) {
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
    }

    @AfterTest
    public void tearDown() throws Exception{
        driver.quit();
    }

    public void openUrl(String url){
        driver.get(url);
    }

    protected void login(String username, String password) {
        // Login inside Jahia
        driver.get(baseUrl + "/cms/login");

        type(By.name("username"), username);
        type(By.name("password"), password);
        click(By.xpath("//a[contains(@href, '#login')]"));
    }

    protected void logout() {
        // Logout
        driver.get(baseUrl + "/cms/logout");
    }

    /**
     * Gets the property value.
     *
     * @param propertyName the property name
     * @param defaultValue the default value
     * @return the property value
     */
    protected static String getPropertyValue(String propertyName, String defaultValue) {
        String value =  System.getProperty(propertyName);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * Type text in an element
     *
     * @param by Element locator
     * @param text Text to type
     */
    public void type(By by, String text){
        WebElement elem = driver.findElement(by);
        try {
            elem.clear();
            elem.sendKeys(text);
        } catch(org.openqa.selenium.StaleElementReferenceException e){
            type(by, text);
        }catch  (org.openqa.selenium.ElementNotVisibleException e2){
            boolean elementDisplayed = false;
            driver.manage().timeouts().implicitlyWait(1, java.util.concurrent.TimeUnit.SECONDS);
            List<WebElement> elems =  driver.findElements(by);
            driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT,java.util.concurrent.TimeUnit.SECONDS);
            for(WebElement element : elems){
                if(element.isDisplayed()){
                    element.clear();
                    element.sendKeys(text);
                    elementDisplayed=true;
                    break;
                }
            }
            if(!elementDisplayed){
                Assert.fail("The element " + by.toString() + "is not visible. " + e2.getMessage());
            }
        }
    }

    public void click(By by){
        WebElement elem = driver.findElement(by);
        try {
            elem.click();
        } catch(org.openqa.selenium.StaleElementReferenceException e){
            click(by);
        }catch  (org.openqa.selenium.ElementNotVisibleException e2){
            boolean elementDisplayed = false;
            driver.manage().timeouts().implicitlyWait(1, java.util.concurrent.TimeUnit.SECONDS);
            List<WebElement> elems =  driver.findElements(by);
            driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
            for(WebElement element : elems){
                if(element.isDisplayed()){
                    element.click();
                    elementDisplayed=true;
                    break;
                }
            }
            if(!elementDisplayed){
                Assert.fail("The element "+by.toString()+ "is not visible and so cannot be clicked. "+e2.getMessage());
            }
        }
    }

    public void verifyElementDisplayed(By element, String message){
        List<WebElement> elements;
        elements  = driver.findElements(element);
        if (elements.size()>0) {
            //some element might be present but not displayed
            for(WebElement web : elements){
                if(web.isDisplayed()){
                    return;
                }
            }
        }
        Assert.fail(message+" " + element.toString());
    }

    public void verifyElementNotDisplayed(By element, String message) {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        List<WebElement> elements;

        elements  = driver.findElements(element);
        if (elements.size()>0) {
            //some element might be present but not displayed
            for(WebElement web : elements){
                if(web.isDisplayed()){
                    driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT, TimeUnit.SECONDS);
                    Assert.fail(message+" " + element.toString());
                }
            }
        }
        driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT, TimeUnit.SECONDS);

    }

    public void verifyTextNotPresent(final String text) {
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String bodyText = driver.findElement(By.cssSelector("BODY")).getText();
                boolean isPresent = bodyText.contains(text);
                return (!isPresent);
            }
        });
    }

    public void verifytextPresent(final String text){
        Assert.assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains(text),
                "Can't find the text: " + text + " on the page " + driver.getCurrentUrl());
    }

    public void verifyLink(By linkLocator, String href){
        Assert.assertTrue(driver.findElement(linkLocator).getAttribute("href").contains(href));
    }
}
