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
        List<Commodity> commodities = homePage.getListOfGoods();
        boolean result = true;
        for(Commodity commodity : commodities){
            if (commodity.getCurrency() != headerCurrency){
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
        List<Commodity> commodities = searchPage.getListOfGoods();
        char car = searchPage.getCurrencyOnHeader();
        boolean result = true;

        for(Commodity commodity : commodities){
            if(commodity.getCurrency() != car){
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
        List<Commodity> commodities = searchPage.getListOfGoods();

        double price = commodities.get(0).getBasicPrice();
        boolean result = true;

        for(Commodity commodity : commodities){
            if(commodity.getBasicPrice() <= price){
                price = commodity.getBasicPrice();
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
        List<Commodity> commodities = searchPage.getListOfGoods();
        boolean result = true;

        for(Commodity commodity : commodities){
            if(commodity.getDiscount() > 0.0){
                double expectedPriceWithDiscount = (Math.round((commodity.getBasicPrice() *
                        ((100.0 - commodity.getDiscount()) / 100.0)) * 100.0) / 100.0);
                if(expectedPriceWithDiscount != commodity.getActualPrice()){
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
