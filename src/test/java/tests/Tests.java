package tests;

import driver_factories.*;
import web_pages.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class Tests {
    Drivers dr = Drivers.OPERA;
    WebDriver driver;
    WebDriverWait wait;


    @Before
    public void setUp() throws NoSuchBrowserDriverException {
        DriverFactory factory = new DriverFactory();
        driver = factory.getDriver(dr);
        wait = new WebDriverWait(driver, 5);
    }


    @Test
    public void checkCurrencyOnHeader(){
        HomePage homePage = new HomePage(driver, wait);
        homePage.open();
        Assert.assertTrue(homePage.atPage());
        char headerCurrency = homePage.getCurrencyOnHeader();
        List<Good> list = homePage.getListOfGoods();
        boolean result = true;
        for(Good good: list){
            if (good.getCurrency() != headerCurrency){
                result = false;
                break;
            }
        }
        Assert.assertTrue("Some goods have unsuitable currency", result);
    }

    @Test
    public void checkNumberOfGoodsOnPage() throws NoSuchOption{
        HomePage homePage = new HomePage(driver, wait);
        homePage.open();
        Assert.assertTrue(homePage.atPage());
        homePage.switchCurrency("usd");
        SearchPage searchPage = homePage.search("dress");
        Assert.assertTrue(searchPage.atPage());
        Assert.assertTrue(searchPage.getGoodsAmount() == searchPage.getListOfGoods().size());
    }

    @Test
    public void checkCurrencyOfGoodsOnPage() throws NoSuchOption{
        HomePage homePage = new HomePage(driver, wait);
        homePage.open();
        Assert.assertTrue(homePage.atPage());
        homePage.switchCurrency("usd");
        SearchPage searchPage = homePage.search("dress");
        Assert.assertTrue(searchPage.atPage());
        List<Good> list = searchPage.getListOfGoods();
        char car = searchPage.getCurrencyOnHeader();
        boolean result = true;

        for(Good good: list){
            if(good.getCurrency() != car){
                result = false;
                break;
            }
        }
        Assert.assertTrue("There are goods with wrong currency.", result);
    }


    @Test
    public void checkGoodsSorting() throws NoSuchOption{
        HomePage homePage = new HomePage(driver, wait);
        homePage.open();
        Assert.assertTrue(homePage.atPage());
        homePage.switchCurrency("usd");
        SearchPage searchPage = homePage.search("dress");
        Assert.assertTrue(searchPage.atPage());
        searchPage.sortGoods("desc");
        List<Good> list = searchPage.getListOfGoods();

        double price = list.get(0).getBasicPrice();
        boolean result = true;

        for(Good good: list){
            if(good.getBasicPrice() <= price){
                price = good.getBasicPrice();
            }
            else{
                result = false;
                break;
            }
        }
        Assert.assertTrue(result);

    }

    @Test
    public void checkDiscountOnPage() throws NoSuchOption{
        HomePage homePage = new HomePage(driver, wait);
        homePage.open();
        Assert.assertTrue(homePage.atPage());
        homePage.switchCurrency("usd");
        SearchPage searchPage = homePage.search("dress");
        Assert.assertTrue(searchPage.atPage());
        searchPage.sortGoods("desc");
        List<Good> list = searchPage.getListOfGoods();
        boolean result = true;

        for(Good good: list){
            if(good.getDiscount() > 0.0){
                double expectedPriceWithDiscount = (Math.round((good.getBasicPrice() *
                        ((100.0 - good.getDiscount()) / 100.0)) * 100.0) / 100.0);
                if(expectedPriceWithDiscount != good.getActualPrice()){
                    result = false;
                    break;
                }
            }
        }
        Assert.assertTrue("There are goods with wrong prices.", result);
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
