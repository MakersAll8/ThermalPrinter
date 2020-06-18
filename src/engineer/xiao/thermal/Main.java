package engineer.xiao.thermal;

import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException {
        // choose line printer to use
        // 选择要使用的打印机
        LinePrinter lp = new LinePrinter();

        // print logo
        // 打印公司图标
        lp.printImage("./taolu.png");

        // print product lines
        // 打印每一行产品列表
        lp.printLines(Helper.sampleLines());

        // print qr code
        // 打印二维码
        lp.printImage("./myqrcode.png");

        lp.printFooter();

    }


}