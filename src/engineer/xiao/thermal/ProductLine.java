package engineer.xiao.thermal;

public class ProductLine {
    private String productEn;
    private String productCn;
    private double price;
    private double quantity;
    private double total;

    public ProductLine(String productEn, String productCn, double price, double quantity) {
        this.productEn = productEn;
        this.productCn = productCn;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    public String getProductEn() {
        return productEn;
    }

    public String getProductCn() {
        return productCn;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public String toString(){
        return productEn + " " + quantity + " " + price
                + "\n" + productCn + "      "+ total;
    }
}
