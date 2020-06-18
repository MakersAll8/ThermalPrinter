package engineer.xiao.thermal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        // use bash to list all line printers
        // 用bash列出所有打印机
        ArrayList<String> availableLp = new ArrayList<>();
        String[] cmd = {
                "/bin/bash",
                "-c",
                "ls /dev/usb | grep lp"};
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        System.out.println("Available line printer devices: ");
        while((line = br.readLine()) != null){
            availableLp.add(line);
            System.out.println(line);
        }

        Scanner kb = new Scanner(System.in);
        String selectedLp;
        do {
            // prompt user to choose a line printer
            // 提示用户选择打印机
            System.out.print("choose your thermal printer: ");
            selectedLp = kb.nextLine();
        } while (!availableLp.contains(selectedLp));

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
        FileOutputStream fs = new FileOutputStream("/dev/usb/"+selectedLp);
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
        DecimalFormat df2 = new DecimalFormat("#.##");
        double total = 0;
        for (ProductLine productLine : productLines) {
            byte[] byteArray = (productLine.getProductEn() + " " + productLine.getProductCn()).getBytes("GBK");
            // change alignment
            ps.write(ZJ58.ESC_Align_Left);
            ps.write(byteArray);
            ps.write(ZJ58.print_and_roll);

            byteArray = (String.format("%.2f", productLine.getPrice()) + " x "
                    + df2.format(productLine.getQuantity()) + " = "
                    + String.format("%.2f", productLine.getTotal())).getBytes("GBK");
            ps.write(ZJ58.ESC_Align_Right);
            ps.write(byteArray);

            ps.write(ZJ58.print_and_roll);

            total += productLine.getTotal();
        }

        // print total
        // 打印总计
        String totalString = "Total 总计： " + String.format("%.2f", total);
        ps.write(totalString.getBytes("GBK"));
        ps.write(ZJ58.print_and_roll);

        ps.write(ZJ58.ESC_Align_Center);
//        ps.write(ZJ58.print_and_roll);

        // print qr code
        // 打印二维码
        ps.write(qrCode);
        ps.write(ZJ58.print_and_roll);

        ps.write("Tech Support 技术支持 ".getBytes("GBK"));
        ps.write(ZJ58.print_and_roll);
        ps.write("https://xiao.engineer".getBytes("GBK"));
        ps.write(ZJ58.print_and_roll);
        ps.write(ZJ58.print_and_roll);
        ps.write(ZJ58.clean_end);

        // send all hex data stream
        // 发送所有16进制数据流
        ps.flush();
        // close the stream
        // 关闭数据流
        ps.close();
    }


}