package web_pages;

public class Good {
    String name;
    char currency;
    double actualPrice;
    double basicPrice;
    double discount;


    public Good(String name, String actualPrice, String basicPrice, String discount){
        this.name = name;
        this.currency = actualPrice.charAt(actualPrice.length() - 1);

        actualPrice = actualPrice.replaceAll("[^\\d||\\,||\\.]+", "" );
        actualPrice = actualPrice.replaceAll("\\,+",".");
        this.actualPrice = Double.parseDouble(actualPrice);
        if (basicPrice == null){
            this.basicPrice = this.actualPrice;
        }
        else{
            basicPrice = basicPrice.replaceAll("[^\\d||\\,||\\.]+", "" );
            basicPrice = basicPrice.replaceAll("\\,+",".");
            this.basicPrice = Double.parseDouble(basicPrice);
        }
        if(discount == null){
            this.discount = 0.0;
        }
        else{
            discount = discount.replaceAll("[^\\d||\\,||\\.]+", "");
            discount = discount.replaceAll("\\,+",".");
            this.discount = Double.parseDouble(discount);
        }


    }

    public String getName() {
        return name;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public double getBasicPrice() {
        return basicPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public char getCurrency() {
        return currency;
    }
}
