import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Herolo {
    static WebDriver webDriver;
    static WebDriverWait wait;
    static Actions actions;
    static SoftAssert Assert;
    static String MAINWINDOW;
    static TakesScreenshot scrShot;

    /** Definitions of objects and navigate to herolo site
     *
     */
    @BeforeClass
    public void definitions() {
        System.setProperty("webdriver.chrome.driver", "/Volumes/Data new/Documents NEW/Development/chromedriver");
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, 15);
        actions = new Actions(webDriver);
        Assert = new SoftAssert();
        scrShot = ((TakesScreenshot) webDriver);

        // Navigate to url
        webDriver.navigate().to("https://automation.herolo.co.il/");
        webDriver.manage().window().maximize();
        Assert.assertEquals(webDriver.getTitle(),"הירולו - חברת פיתוח מובילה המתמחה בפתרונות פרונט אנד");

        // Save the main window in a variable
        MAINWINDOW = webDriver.getWindowHandle();
    }

    /** This test does positive tests in the main page
     *
     * @throws Exception
     */
    @Test (priority = 0)
    public void mainPage() throws Exception {

        // Check if buttons in portfolio are displayed
        webDriver.findElement(By.xpath("//div[@class = 'slick-arrow slick-next']")).isDisplayed();
        webDriver.findElement(By.xpath("//div[@class = 'slick-arrow slick-prev']")).isDisplayed();

        // Move the page down
        actions.moveToElement(webDriver.findElement(By.xpath("//h2[@class = 'typography__Title-t0yuqj-1 gaeKRO']"))).click();
        actions.sendKeys(Keys.PAGE_DOWN).perform();

        // Check if back to top button is displayed and work by taking snap shot
        WebElement backToTop = webDriver.findElement(By.xpath("//a[@class = 'backToTop__BtnGoUp-sc-1deq75d-0 fIqtKc']")) ;
        backToTop.isDisplayed();
        takeSnapShot(webDriver,"Before click back to top");
        backToTop.click();

        // Wait for the scroller to get to the top of the page and take a snap shot
        Thread.sleep(1000);
        takeSnapShot(webDriver,"After click back to top");

        // Filling contact us section
        webDriver.findElement(By.xpath("//input[@id = 'name']")).sendKeys("oz");
        webDriver.findElement(By.xpath("//input[@id = 'company']")).sendKeys("Sharon");
        webDriver.findElement(By.xpath("//input[@id = 'email']")).sendKeys("oz@gmail.com");
        webDriver.findElement(By.xpath("//input[@id = 'telephone']")).sendKeys("0541234567");
        webDriver.findElement(By.xpath("//a[@class = 'commun__Button-mgrfny-0 commun__ButtonContact-mgrfny-1" +
                " form__ButtonContact-sc-1ju2h8q-1 gGWtQr']")).click();

        // Take snap shot of the page and return to main page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class = 'thankYou__title-bl9e43-5 fSmmAW']")));
        takeSnapShot(webDriver, "HelpPage");
        webDriver.findElement(By.xpath("//a[@class = 'thankYou__backLink-bl9e43-10 idImZT']")).click();
    }

    /** This test check the thank you page
     *
     * @throws Exception
     */
    @Test (priority = 1)
    public void thankYouPage() throws Exception {

        // Filling help footer - positive values
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer[@id = 'footer']")));
        webDriver.findElement(By.xpath("//input[@name = 'name']")).sendKeys("oz");
        webDriver.findElement(By.xpath("//input[@name = 'email']")).sendKeys("oz@gmail.com");
        webDriver.findElement(By.xpath("//input[@name = 'phone']")).sendKeys("0541234567");
        webDriver.findElement(By.xpath("//button[@class = 'Footer__Button-sc-1xqajj9-7 jixtxJ']")).click();

        // Wait and take snap shot of thank you page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class = 'thankYou__title-bl9e43-5 fSmmAW']")));
        takeSnapShot(webDriver, "HelpPage");

        // Check text with assert
        Assert.assertEquals(webDriver.findElement(By.xpath("//h1[@class = 'thankYou__title-bl9e43-5 fSmmAW']")).getText(),"תודה!");
        Assert.assertEquals(webDriver.findElement(By.xpath("//span[contains(text(),'הנתונים התקבלו בהצלחה, ניצור קשר ממש בקרוב…')]")).getText(),
                "הנתונים התקבלו בהצלחה, ניצור קשר ממש בקרוב…");
        Assert.assertEquals(webDriver.findElement(By.xpath("//span[contains(text(),'בנתיים תוכלו להציץ אצלנו באתר ולראות מה אנחנו שווים')]")).getText(),
                "בנתיים תוכלו להציץ אצלנו באתר ולראות מה אנחנו שווים");

        // Check if buttons displayed
        WebElement backButton = webDriver.findElement(By.xpath("//a[@href = '/']"));
        backButton.isDisplayed();
        WebElement moveToSite = webDriver.findElement(By.xpath("//button[@class = 'thankYou__button-bl9e43-9 NQqYi']"));
        moveToSite.isDisplayed();
        Assert.assertEquals(moveToSite.getText(), "עבור לאתר");

        // Moving to company page
        moveToSite.click();

        // Handles the opening of the new window
        ArrayList<String> newTab = new ArrayList<String>(webDriver.getWindowHandles());
        newTab.remove(MAINWINDOW);
        webDriver.switchTo().window(newTab.get(0));

        // Take snap shot of the new window and close it
        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath("//a[@class = 'navbar-brand']"))));
        takeSnapShot(webDriver, "Company Page");
        webDriver.close();
        webDriver.switchTo().window(MAINWINDOW);

        // Return to main page
        backButton.click();
    }

    /** This test check the what'sapp page
     *
     * @throws Exception
     */
    @Test (priority = 2)
    public void whatsApp() throws Exception {

        // What'sapp button
        webDriver.findElement(By.xpath("//a[@class = 'callUsWhatsapp__BtnWhatsapp-sc-1bcgurk-0 cPQmgB']")).click();

        Assert.assertEquals(webDriver.findElement(By.xpath("//p")).getText(),
                "Chat on WhatsApp with +972 54-494-5333");
        webDriver.findElement(By.xpath("//a[@id = 'action-button']")).isDisplayed();

        // Handles the opening of the new window
        ArrayList<String> newTab = new ArrayList<String>(webDriver.getWindowHandles());
        newTab.remove(MAINWINDOW);
        webDriver.switchTo().window(newTab.get(0));

        // Take snap shot of the new window and close it
        takeSnapShot(webDriver, "What'sapp page");
        webDriver.close();
        webDriver.switchTo().window(MAINWINDOW);
    }

    /** This test check the if the correct text is displayed in the main page
     *
     */
    @Test (priority = 3)
    public void textCheckMainPage() {

        // Head
        Assert.assertEquals(webDriver.findElement(By.xpath
                ("//h2[@class = 'logo__Text-sc-1gco7ve-3 pbETZ']")).getText(), "מפתחים");
        Assert.assertEquals(webDriver.findElement(By.xpath
                ("//h2[@class = 'logo__Text-sc-1gco7ve-3 loXSYC']")).getText(), " בשפה שלך");

        Assert.assertEquals(webDriver.findElement(By.xpath
                        ("//h2[@class = 'typography__Title-t0yuqj-1 typography__DesktopTitle-t0yuqj-2 gtgKRY']//span")).getText(),
                "הירולו - מובילים בפיתוח");

        Assert.assertEquals(webDriver.findElement(By.xpath
                        ("//h1[@class = 'introduction__H1-sc-1sqfjhg-3 introduction__TextDesktop-sc-1sqfjhg-4 fBcRth']")).getText(),
                "הירולו היא חברת פיתוח מובילה המתמחה בפתרונות Front-End ו-Full Stack.");

        Assert.assertEquals(webDriver.findElements(By.xpath
                        ("//h4[@class = 'introduction__Text-sc-1sqfjhg-2 jXQBtc']//span")).get(0).getText(),
                "עד היום, בנינו מאות אפליקציות ווב ומובייל עבור עשרות לקוחות באמצעות הטכנולוגיות החדישות ביותר בתעשייה.");
        Assert.assertEquals(webDriver.findElements(By.xpath
                        ("//h4[@class = 'introduction__Text-sc-1sqfjhg-2 jXQBtc']//span")).get(1).getText(),
                "אם אתם זקוקים לפיתוח מכל סוג או הרחבה וחיזוק צוות הפיתוח שלכם – הגעתם למקום הנכון.");

        // How can we help you
        Assert.assertEquals(webDriver.findElements(By.xpath("//h2[@class = 'typography__Title-t0yuqj-1 jDEOuK']//span")).get(0),"איך נוכל לעזור לכם?");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h3[@class = 'serviceCard__Title-sc-1fbxoyv-3 jfCXaX']//span")).get(0),"מיקור חוץ לפיתוח Frontend ובודקי איכות");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(0),
                "אנחנו בונים צוותים המורכבים ממפתחי Full Stack, צד לקוח (React, Angular, Vue)," +
                        " בודקי איכות (ידני ואוטומציה) ומנהלי פרויקטים. בעזרתנו תוכלו להקים במהירות צוות טכנולוגי איכותי וממוקצע.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h3[@class = 'serviceCard__Title-sc-1fbxoyv-3 jfCXaX']//span")).get(1),"פרויקט Full Stack מקצה לקצה");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(1),
                "אנחנו בונים צוות המותאם לפי הצרכים שלכם ודואגים לתהליך הפיתוח מקצה לקצה." +
                        " מנהלי הפרויקטים שלנו דואגים לנהל את הפרויקט בצורה הנכונה והאפקטיבית ביותר. עובדים בספרינטים ומתודולוגיות Agile.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h3[@class = 'serviceCard__Title-sc-1fbxoyv-3 jfCXaX']//span")).get(2),"ייעוץ ושדרוג ל–JavaScript");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(2),
                "אנחנו יכולים לשדרג לכם את האתר ו/או האפליקציה שלכם, לדאוג שתהיו מעודכנים עם הטכנולוגיות החדשות ביותר ולבחון את בסיס הקוד יחד איתכם.");

        // Work for example
        Assert.assertEquals(webDriver.findElement(By.xpath("//h2[@class = 'typography__Title-t0yuqj-1 gaeKRO']")),"עבודות לדוגמה");

        // Some of our customers
        Assert.assertEquals(webDriver.findElements(By.xpath("//h2[@class = 'typography__Title-t0yuqj-1 jDEOuK']//span")).get(1),"כמה מהלקוחות שלנו");

        // Answer for every question
        Assert.assertEquals(webDriver.findElement(By.xpath("//h2[@class = 'typography__Title-t0yuqj-1 " +
                "questions__Title-cjtemb-3 duDHMi']")),"לכל שאלה תשובה");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(0),
                "אנחנו זקוקים לחיזוק והרחבת צוות הפיתוח הקיים, האם זה משהו שאתם יכולים לעזור בו?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(3),
                "בהחלט. צוות הפיתוח שלנו מונה עשרות מפתחים ובודקי איכות ואנחנו יודעים לעבוד מרחוק או ממשרדי הלקוח.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(1),
                "האם אתם מספקים רק שירותי פיתוח צד לקוח?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(4),
                "לא רק. הירולו היא חברת פיתוח מובילה המספקת שירותי פיתוח מקצה לקצה. יש לנו צוות גדול של" +
                        " מפתחים, מנהלי פרויקטים, בודקי איכות (ידני ואוטומציה), ארכיטקטים ומנהלי צוותים. אנחנו כאן לכל פרויקט שלכם.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(2),
                "האם יש לכם ניסיון עם חברות סטארטאפ?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(5),
                "מאז הקמת הירולו ב-2009, ליווינו עשרות חברות סטארטאפ עם פתרונות מיקור חוץ, פיתוח מרחוק או ייעוץ.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(3),
                "האם המפתחים שלכם עובדים ממקומות אחרים בעולם?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(6),
                "הירולו חרתה על דגלה להשאיר את ההייטק בארץ. כל עובדי החברה הם מנוסים, ממוקצעים ותוצרת כחול לבן.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(4),
                "למה כדאי לי לעבוד דווקא איתכם?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(7),
                "בגלל הניסיון הרב, המוניטין ומתודולוגיית העבודה הייחודית שלנו. אנחנו עוזרים ללקוחותינו להוריד את עומס העבודה ובכך הם חוסכים לעצמם המון זמן וכסף." +
                        " אנחנו עוזרים להבטיח עמידה בלוחות הזמנים של הפרויקט/המוצר ועוזרים בגיוס של מתכנתים ממוקצעים להרחבת הצוות הקיים.");

        Assert.assertEquals(webDriver.findElements(By.xpath("//h4[@class = 'questionCard__Title-sc-1silg10-1 lafykM']//span")).get(5),
                "איך נוכל להתקדם?");
        Assert.assertEquals(webDriver.findElements(By.xpath("//p[@class = 'typography__P-t0yuqj-0 fTLONu']//span")).get(8),
                "תחילה, נתאם פגישת היכרות בה נכיר אחד את השני, נבין את הצרכים והיעדים שלכם ונוכל לפרט על תהליך העבודה הנכון ביותר להמשך התהליך.");
    }

    /** This method take a screen shot of the current page
     *
     * @param webdriver - Web driver
     * @param testName - The name of the test file for the picture
     * @throws Exception
     */
    public static void takeSnapShot(WebDriver webdriver,String testName) {

        // Get current time & date
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

        // Take screen shot of current page and move it to specific location
        File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("/Volumes/Data new/Documents NEW/Development/Project " + testName + "_" + dateFormat.format(date)+".png"));
        }
        catch (IOException e) {
            System.out.println("Throw exception: "+e);
        }
    }

    /** Quit the browser
     *
     */
    @AfterClass
    public void quit() {
        webDriver.quit();
    }
}
