package web_pages;

import com.sun.istack.internal.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class SearchPage implements WebPage{
    final static Logger logger = Logger.getLogger(HomePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String ADDRESS = "http://prestashop-automation.qatestlab.com.ua/ru/search";
    private static final By SEARCH_FIELD = By.xpath("//input[@placeholder='Поиск в каталоге']");
    private static final By SEARCH_BUTTON = By.xpath("//div[@id='search_widget']//i[@class='material-icons search']") ;
    private static final By CURRENCY_BUTTON = By.xpath("//div[@class='currency-selector dropdown js-dropdown']" +
            "//i[@class='material-icons expand-more']");
    private static final By CURRENCY_ON_HEADER = By.xpath("//span[@class='expand-more _gray-darker hidden-sm-down']");
    private static final By EUR = By.xpath("//a[contains(text(),'EUR')]");
    private static final By UAN = By.xpath("//a[contains(text(),'UAH')]");
    private static final By USD = By.xpath("//a[contains(text(),'USD $')]");
    private static final By SORT_BUTTON = By.xpath("//i[@class='material-icons pull-xs-right']");
    private static final By DESC = By.xpath("//a[contains(text(), \"Цене: от высокой к низкой\")]");
    private static final By ASC = By.xpath("//div[@id='js-product-list-top']//a[contains(text(), \"Цене: от низкой к высокой\")]");
    private static final By GOODS_AMOUNT = By.xpath("//p[contains(text(), \"Тов\")]");
    private static final By PRODUCT_DESCRIPTION = By.xpath("//div[@class=\"product-description\"]");
    private static final By PRICE = By.xpath("//span[@class=\"price\"]");
    private static final By CHECK_SORT_DESC = By.xpath("//div[@class = \"col-sm-12 col-xs-12 col-md-9 products-sort-order dropdown\"]" +
            "/a[contains(text(), \"от высокой\")]");
    private static final By CHECK_SORT_ASC = By.xpath("//div[@class = \"col-sm-12 col-xs-12 col-md-9 products-sort-order dropdown\"]" +
            "/a[contains(text(), \"от низкой\")]");

    public SearchPage(WebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
    }

    @Override
    public void open(){
        driver.get(ADDRESS);
        driver.manage().window().maximize();
        logger.info("Search page is opened");
    }

    public void search(String str){
        driver.findElement(SEARCH_FIELD).clear();
        driver.findElement(SEARCH_FIELD).sendKeys(str);
        driver.findElement(SEARCH_BUTTON).click();
        logger.info("The field 'search' was filled and pressed");
    }

    public void switchCurrency(String currency) throws NoSuchOption{
        driver.findElement(CURRENCY_BUTTON).click();
        if (currency.equalsIgnoreCase("eur")){
            driver.findElement(EUR).click();
            logger.info("Currency was switched");
            return;
        }
        if (currency.equalsIgnoreCase("uan")){
            driver.findElement(UAN).click();
            logger.info("Currency was switched");
            return;
        }
        if (currency.equalsIgnoreCase("usd")){
            driver.findElement(USD).click();
            logger.info("Currency was switched");
            return;
        }
        throw new NoSuchOption("There is no such a currency.");
    }

    public void sortGoods(String clue) throws NoSuchOption{
        driver.findElement(SORT_BUTTON).click();
        if(clue.equalsIgnoreCase("desc")){
            driver.findElement(DESC).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(CHECK_SORT_DESC));
            return;
        }
        if (clue.equalsIgnoreCase("asc")){
            driver.findElement(ASC).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(CHECK_SORT_ASC));
            return;
        }
        throw new NoSuchOption("There is no such option");
    }

    public int getGoodsAmount(){
        String str = driver.findElement(GOODS_AMOUNT).getText();
        str = str.replaceAll("\\D+","");
        int amount = Integer.parseInt(str);
        logger.info("The amount of goods on the page was taken");
        return amount;
    }
    public char getCurrencyOnHeader(){
        String cur = driver.findElement(CURRENCY_ON_HEADER).getText();
        char currency = cur.charAt(cur.length() - 1);
        logger.info("Currency was got from Header");
        return currency;
    }

    public List<Good> getListOfGoods(){
        List<WebElement> list = driver.findElements(PRODUCT_DESCRIPTION);
        List<Good> goodsList = new ArrayList<Good>();

        for(int i = 0; i < list.size(); i++){

            String realPrice = null;
            String actualPrice = null;
            String name = null;
            String discount = null;

            name = list.get(i).findElement(By.xpath("./h1/a")).getText();
            actualPrice = list.get(i).findElement(By.xpath("./div/span[@class=\"price\"]")).getText();

            if(list.get(i).findElements(By.xpath("./div/span[@class=\"regular-price\"]")).size() > 0){
                realPrice = list.get(i).findElement(By.xpath("./div/span[@class=\"regular-price\"]")).getText();
            }
            if(list.get(i).findElements(By.xpath("./div/span[@class=\"discount-percentage\"]")).size() > 0){
                discount = list.get(i).findElement(By.xpath("./div/span[@class=\"discount-percentage\"]")).getText();
            }

            Good good = new Good(name, actualPrice, realPrice, discount);
            goodsList.add(good);

        }
        logger.info("A List of goods on the page was created and sent");
        return goodsList;
    }
    @Override
    public boolean atPage(){
        return driver.getTitle().equals("Поиск");
    }
}
