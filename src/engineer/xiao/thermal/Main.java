package engineer.xiao.thermal;

import com.google.zxing.WriterException;

import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException {
        String propertiesPath;
        if (args.length != 1) {
            System.out.println("使用方式： java -jar ThermalPrinter.jar <配置文档路径>");
            System.out.println("使用默认配置文件 ./config.properties");
            propertiesPath = "./config.properties";
        } else {
            propertiesPath = args[0];
        }
        CommonUtil.loadProperties(propertiesPath);

        // choose line printer to use
        // 选择要使用的打印机
        LinePrinter lp = new LinePrinter();
        ReceiptServer.lp = lp;

        // start the print receipt request server
        ReceiptServer server = new ReceiptServer();
        server.start();

        do {
            // wait for qr code scan
            String qrId = QrScanner.read();
            // print the qr id
            lp.printReceipt(qrId);
        } while (true);

    }

    void printTest(LinePrinter lp) throws IOException {
        lp.alignCenter();
        // Test Print
        lp.font_size_double();
        lp.bold();
        lp.printLine("中文");
//        lp.printLine("EN");
        lp.cancel_bold();
        lp.font_size_normal();
        lp.roll();

        // print qr code
        // 打印二维码
        int width = 350;
        int height = 350;
        String data = "https://xiao.engineer";
        try {
            byte[] qrCode = QrCode.getQRCodeImage(data, width, height);
            lp.printImage(qrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        lp.printFooter(false);
    }


}