import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    capabilities.setCapability("app", "/Users/vasilkova/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");

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


    waitForElementPresent(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Cannot find Search element",
            5
    );

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
            "Cannot find Search Wikipedia element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search... element",
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

  @Test
  public void searchForArticlesAndCancelTest() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search... element",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find articles",
            15
    );

    int numberOfArticles = findElementsAndReturnNumber(
            By.id("org.wikipedia:id/page_list_item_container")
    );

    Assert.assertEquals(
            true,
            numberOfArticles > 1
    );

    waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Cannot find field to clear",
            5

    );

    waitForElementNotPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'"),
            "The result is still presented",
            5
    );

  }

  @Test
  public void searchForTextInResultTest() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Cannot find Search... element",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Cannot find articles",
            15
    );

    List<WebElement> numberOfTitlesWithSearchedText = driver.findElements(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title']")
    );

    for (int i = 0; i < numberOfTitlesWithSearchedText.size(); ++i) {
      WebElement title = numberOfTitlesWithSearchedText.get(i);
      boolean titleContainsText = title.getAttribute("text").contains("Java");
      Assert.assertTrue(titleContainsText);
    }
  }

  @Test
  public void swipeArticle() {

    waitForElementPresentAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Cannot find Search Wikipedia element",
            5
    );

    waitForElementPresentAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Appium",
            "Cannot find Search... element",
            5
    );

    waitForElementPresentAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
            "Cannot find Search element",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Cannot find element Title",
            10

    );

    swipeUpToFindElement(
            By.xpath("//*[@text='View page in browser']"),
            "Cannot find the end of article",
            20
    );

  }

  private WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
    wait.withMessage(error_message + "\n");
    return wait.until(
            ExpectedConditions.presenceOfElementLocated(by));
  }

  private WebElement waitForElementPresent(By by, String error_message) {
    return waitForElementPresent(by, error_message, 5);
  }

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

  private int findElementsAndReturnNumber(By by) {
     List elements = driver.findElements(by);
     int elements_number = elements.size();
     return elements_number;
  }

  protected void swipeUp(int timeOfSwipe) {
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    int x = size.width / 2;
    int start_y =  (int) (size.height * 0.8);
    int end_y = (int) (size.height * 0.2);

    action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
  }
  protected void  swipeUpQuick() {
    swipeUp(200);
  }

  protected void swipeUpToFindElement(By by, String error_message, int max_swiped){
    int already_swiped = 0;
    while (driver.findElements(by).size() == 0){

      if (already_swiped > max_swiped){
        waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
        return;
      }
      swipeUpQuick();
      ++already_swiped;
    }
  }

}
