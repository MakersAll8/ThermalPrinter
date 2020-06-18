package engineer.xiao.thermal;

import java.util.ArrayList;

public class Helper {
    static ArrayList<ProductLine> sampleLines(){
        // create an array list of product lines
        // 创建代表产品列表每一行的一个数组
        ArrayList<ProductLine> productLines = new ArrayList<>();
        productLines.add(
                new ProductLine("Nike Flight 89", "耐克男子篮球鞋", 170.0, 2.00)
        );
        productLines.add(
                new ProductLine("Nike Maxin 200", "耐克Maxin 200", 220.0, 2.00)
        );
        productLines.add(
                new ProductLine("Nike Air Force 1 High", "耐克空军一号高帮", 190.0, 3.50)
        );

        productLines.add(
                new ProductLine("Raspberry Pi 4B", "树莓派4B", 230, 5.00)
        );

        productLines.add(
                new ProductLine("Orange Pi", "橙子派", 120, 6.00)
        );

        productLines.add(
                new ProductLine("ScanHom QR Code Scanner", "二维码识别器", 150, 11.00)
        );

        return productLines;
    }


}
