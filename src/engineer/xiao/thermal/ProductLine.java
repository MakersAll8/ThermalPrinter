package engineer.xiao.thermal;

public class ProductLine {
    private String productEn;
    private String productCn;
    private double price;
    private double quantity;
    private double total;

    ProductLine(String productEn, String productCn, double price, double quantity) {
        this.productEn = productEn;
        this.productCn = productCn;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    String getProductEn() {
        return productEn;
    }

    String getProductCn() {
        return productCn;
    }

    double getPrice() {
        return price;
    }

    double getQuantity() {
        return quantity;
    }

    double getTotal() {
        return total;
    }

    public String toString(){
        return productEn + " " + quantity + " " + price
                + "\n" + productCn + "      "+ total;
    }
}
