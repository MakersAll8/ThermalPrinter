package engineer.xiao.thermal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException {

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
                new ProductLine("Nike Air Force 1 High", "耐克空军一号高帮", 190.0, 3.00)
        );

        // load logo and qr code
        // 加载公司图标和二维码到内存
        BufferedImage bufferedImage = ImageIO.read(new File("./taolu.png"));
        byte[] blackWhitePix = ZJ58.processImage(bufferedImage);
        byte[] logo = ZJ58.eachLinePixToCmd(blackWhitePix, ZJ58.printWidth, ZJ58.nMode);

        BufferedImage qrImage = ImageIO.read(new File("./myqrcode.png"));
        byte[] qrPix = ZJ58.processImage(qrImage);
        byte[] qrCode = ZJ58.eachLinePixToCmd(qrPix, ZJ58.printWidth, ZJ58.nMode);

        // open stream connection to line printer
        // 打开连接打印机的数据流
        FileOutputStream fs = new FileOutputStream("/dev/usb/lp2");
        PrintStream ps = new PrintStream(fs);
        // sending hex code to initialize the thermal printer. Note, this clears
        // encoding settings as well. I believe default is ASCII.
        // 发送初始化的16进制代码到热敏打印机。注意，这会把字符编码也重置。默认编码应该是ASCII
        ps.write(ZJ58.init);
        // set encoding to GBK to print Chinese
        // 设置编码为GBK打印中文
        ps.write(ZJ58.zh_mode);

        // print logo
        // 打印公司图标
        ps.write(logo);
        ps.write(ZJ58.print_and_roll);

        // print product lines
        // 打印每一行产品列表
        double total = 0;
        for (ProductLine productLine : productLines) {
            byte[] byteArray = productLine.toString().getBytes("GBK");
            ps.write(byteArray);
            ps.write(ZJ58.print_and_roll);
            total += productLine.getTotal();
        }

        // print total
        // 打印总计
        String totalString = "Total 总计： " + total;
        ps.write(totalString.getBytes("GBK"));
        ps.write(ZJ58.print_and_roll);

        // print qr code
        // 打印二维码
        ps.write(qrCode);
        ps.write(ZJ58.clean_end);

        // send all hex data stream
        // 发送所有16进制数据流
        ps.flush();
        // close the stream
        // 关闭数据流
        ps.close();
    }


}