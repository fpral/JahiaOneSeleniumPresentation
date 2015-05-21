package org.jahia.selenium;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Base class for testing modules.
 */
public abstract class ModuleTest {
    private static final long WEB_DRIVER_TIMEOUT = 20; //in seconds
    protected WebDriver driver;
    protected String baseUrl;

    public ModuleTest() {
        this.baseUrl = new StringBuilder("http://").append(getPropertyValue("selenium.jahia.host", "localhost"))
                .append(":").append(getPropertyValue("selenium.jahia.port", "8080"))
                .append(getPropertyValue("selenium.jahia.context", "")).toString();
    }

    @BeforeTest
    public void setUpTest(){
        if (driver == null) {
            driver = new FirefoxDriver();
           // driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
    }

    @AfterClass
    public void tearDown() throws Exception{
        driver.quit();
    }

    /**
     * Open the given URL
     * @param url
     */
    public void openUrl(String url){
        driver.get(url);
    }

    /**
     * Log in Digital Factory
     * @param username
     * @param password
     */
    protected void login(String username, String password) {
        driver.get(baseUrl + "/cms/login");
        type(By.name("username"), username);
        type(By.name("password"), password);
        click(By.xpath("//a[contains(@href, '#login')]"));
    }

    /**
     * Logout from Digital Factory
     */
    protected void logout() {
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
     * Type text in an element.
     *
     * @param by Element locator
     * @param text Text to type
     */
    public void type(By by, String text){
        WebElement elem = driver.findElement(by);
        elem.clear();
        elem.sendKeys(text);
    }

    /**
     * Click on an element
     * @param by
     */
    public void click(By by){
        driver.findElement(by).click();
    }


    /**
     * Verify that an element is not displayed
     * @param element
     */
    public void verifyElementNotDisplayed(By element) {
        //We reduce the webdriver timeout to avoid waiting when calling findElements
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        List<WebElement> elements = driver.findElements(element);
        if (elements.size()>0) {
            //some element might be present but not displayed
            for(WebElement web : elements){
                if(web.isDisplayed()){
                    driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT, TimeUnit.SECONDS);
                    Assert.fail("The following element should not be displayed: " + element.toString());
                }
            }
        }
        driver.manage().timeouts().implicitlyWait(WEB_DRIVER_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * Verify that the given text is not displayed
     * @param text
     */
    public void verifyTextNotPresent(final String text) {
        (new WebDriverWait(driver, WEB_DRIVER_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String bodyText = driver.findElement(By.cssSelector("BODY")).getText();
                boolean isPresent = bodyText.contains(text);
                return (!isPresent);
            }
        });
    }

    /**
     * Verify that the given text is present in the page
     * @param text
     */
    public void verifyTextPresent(final String text){
        Assert.assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains(text),
                "Can't find the text: " + text + " on the page " + driver.getCurrentUrl());
    }

    /**
     * Verify that the link appears and also verify its href attribute
     * @param linkLocator
     * @param href
     */
    public void verifyLink(By linkLocator, String href){
        Assert.assertTrue(driver.findElement(linkLocator).getAttribute("href").contains(href));
    }
}
