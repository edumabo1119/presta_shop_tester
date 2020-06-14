package driver_factories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    WebDriver driver;
    public WebDriver getDriver(Drivers dr) throws NoSuchBrowserDriverException {
        switch(dr){
            case OPERA:
                System.setProperty("webdriver.opera.driver",
                        "src/main/resources/drivers/operadriver.exe");
                driver = new OperaDriver();
                driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                return driver;

            case CHROME:
                System.setProperty("webdriver.chrome.driver",
                        "src/main/resources/drivers/chromedriver.exe");
                driver = new ChromeDriver();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                return driver;

            case FIREFOX:
                System.setProperty("webdriver.gecko.driver",
                        "src/main/resources/drivers/geckodriver.exe");
                driver = new FirefoxDriver();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                return driver;
        }
        throw new NoSuchBrowserDriverException("There is no such a driver!");
    }
}
