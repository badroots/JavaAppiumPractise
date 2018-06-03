import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest {

  private AppiumDriver driver;

  @Before
  public void setUp() throws Exception {
    DesiredCapabilities capabilities = new DesiredCapabilities();

    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "6.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    capabilities.setCapability("app", "/Users/vasilkova/Documents/GitHub/JavaAppiumPractise/JavaAppiumAutomation/apks/org.wikipedia.apk");

    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void searchPlaceholderTest() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search element",
            5
    );


    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot find Search item  with text 'Object-oriented programming language",
            15
    );
  }

  @Test
  public void cancelSearchTest() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search element",
            5
    );

    waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Cannot find field to clear",
            5

    );

    waitForElementPresentAndClick(
            By.id("org.wikipedia:id/search_close_btn"),
            "Cannot find X cancel element",
            5
    );

    waitForElementNotPresent(
            By.id("org.wikipedia:id/search_close_btn"),
            "The X button still presented",
            5
    );

  }

  @Test
  public void compareArticleTitle() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search element",
            5
    );

    waitForElementPresentAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
            "Cannot find Search element",
            5
    );

    WebElement title_element = waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find Java article header",
            15
    );

    String article_title = title_element.getAttribute("text");

    Assert.assertEquals(
            "Expected message does not match actual",
            "Java (programming language)",
            article_title
    );

  }

//  private WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
//    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
//    wait.withMessage(error_message + "\n");
//    return wait.until(
//            ExpectedConditions.presenceOfElementLocated(by));
//  }
//
//  private WebElement waitForElementPresent(By by, String error_message) {
//    return waitForElementPresent(by, error_message, 5);
//  }

  private WebElement waitForElementPresentAndClick(By by, String error_message, long timeInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementPresentAndSendKeys(By by, String value, String error_message, long timeInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
    element.sendKeys(value);
    return element;
  }

  private boolean waitForElementNotPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(
            ExpectedConditions.invisibilityOfElementLocated(by));
  }

  private WebElement waitForElementAndClear(By by, String error_message, long timeInSeconds) {
    WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
    element.clear();
    return element;

  }

}
