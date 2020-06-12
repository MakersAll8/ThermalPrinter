package engineer.xiao.thermal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException {


        ArrayList<ProductLine> productLines = new ArrayList<>();
        productLines.add(
                new ProductLine("Nike Flight 89", "耐克男子篮球鞋", 170.0, 2.00)
        );
        productLines.add(
                new ProductLine("Nike Maxin 200", "耐克Maxin 200", 220.0, 2.00)
        );
        productLines.add(
                new ProductLine("Nike Air Force 1 High", "耐克空军一号高帮", 190.0, 3.00)
        );

        // load image
        BufferedImage bufferedImage = ImageIO.read(new File("./taolu.png"));
        byte[] blackWhitePix = ZJ58.processImage(bufferedImage);
        byte[] logo = ZJ58.eachLinePixToCmd(blackWhitePix, ZJ58.printWidth, ZJ58.nMode);

        BufferedImage qrImage = ImageIO.read(new File("./myqrcode.png"));
        byte[] qrPix = ZJ58.processImage(qrImage);
        byte[] qrCode = ZJ58.eachLinePixToCmd(qrPix, ZJ58.printWidth, ZJ58.nMode);

        FileOutputStream fs = new FileOutputStream("/dev/usb/lp2");
        PrintStream ps = new PrintStream(fs);
        ps.write(ZJ58.init);
        ps.write(ZJ58.zh_mode);

        ps.write(logo);
        ps.write(ZJ58.print_and_roll);

        double total = 0;
        for (ProductLine productLine : productLines) {
            byte[] byteArray = productLine.toString().getBytes("GBK");
            ps.write(byteArray);
            ps.write(ZJ58.print_and_roll);
            total += productLine.getTotal();
        }

        String totalString = "Total 总计： " + total;
        ps.write(totalString.getBytes("GBK"));
        ps.write(ZJ58.print_and_roll);

        ps.write(qrCode);
        ps.write(ZJ58.clean_end);

//        ps.write(reset);
//        ps.write(reset);

        ps.flush();
    }


}