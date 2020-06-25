package engineer.xiao.thermal;

import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

class LinePrinter {
    private String selectedLp;
    private ArrayList<String> availableLp = new ArrayList<>();
    private PrintStream printStream = null;
    static String lpId;

    LinePrinter() throws IOException {
        selectLp();
        initializePrinter();
    }

    void selectLp(){
        try {
            // use bash to list all line printers
            // 用bash列出所有打印机
            String[] cmd = {
                    "/bin/bash",
                    "-c",
                    "ls /dev/usb | grep lp"};
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            System.out.println("打印设备列表： ");
            while((line = br.readLine()) != null){
                availableLp.add(line);
                System.out.println(line);
            }

//            Scanner kb = new Scanner(System.in);
//            do {
//                // prompt user to choose a line printer
//                // 提示用户选择打印机
//                System.out.print("Choose your thermal printer: ");
//                selectedLp = kb.nextLine();
//            } while (!availableLp.contains(selectedLp));
            System.out.println("正在使用配置默认打印机"+lpId);
            selectedLp = lpId;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    PrintStream getPrinter() throws FileNotFoundException {
        if(printStream == null){
            // open stream connection to line printer
            // 打开连接打印机的数据流
            FileOutputStream fs = new FileOutputStream("/dev/usb/"+selectedLp);
            printStream = new PrintStream(fs);
        }
        return printStream;

    }

    void initializePrinter() throws IOException {
        // sending hex code to initialize the thermal printer. Note, this clears
        // encoding settings as well. I believe default is ASCII.
        // 发送初始化的16进制代码到热敏打印机。注意，这会把字符编码也重置。默认编码应该是ASCII
        getPrinter().write(ZJ58.init);
        // set encoding to GBK to print Chinese
        // 设置编码为GBK打印中文
        getPrinter().write(ZJ58.zh_mode);
    }

    void printImage(String imagePath){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(imagePath));
            byte[] blackWhitePix = ZJ58.processImage(bufferedImage);
            byte[] image = ZJ58.eachLinePixToCmd(blackWhitePix, ZJ58.printWidth, ZJ58.nMode);
            getPrinter().write(image);
            getPrinter().write(ZJ58.print_and_roll);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void roll() throws IOException {
        getPrinter().write(ZJ58.print_and_roll);
    }

    void printImage(String imagePath, int shrinkFactor){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(imagePath));
            byte[] blackWhitePix = ZJ58.processImage(bufferedImage, shrinkFactor);
            byte[] image = ZJ58.eachLinePixToCmd(blackWhitePix, ZJ58.printWidth, ZJ58.nMode);
            getPrinter().write(ZJ58.ESC_Align_Center);
            getPrinter().write(image);
            getPrinter().write(ZJ58.print_and_roll);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void printImage(byte[] imageBytes){
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            byte[] blackWhitePix = ZJ58.processImage(bufferedImage);
            byte[] image = ZJ58.eachLinePixToCmd(blackWhitePix, ZJ58.printWidth, ZJ58.nMode);
            getPrinter().write(image);
            getPrinter().write(ZJ58.print_and_roll);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void printLines(ArrayList<ProductLine> productLines) throws IOException {
        DecimalFormat df2 = new DecimalFormat("#.##");
        double total = 0;
        for (ProductLine productLine : productLines) {
            byte[] byteArray = (productLine.getProductEn() + " " + productLine.getProductCn()).getBytes("GBK");
            // change alignment
            getPrinter().write(ZJ58.ESC_Align_Left);
            getPrinter().write(byteArray);
            getPrinter().write(ZJ58.print_and_roll);

            byteArray = (String.format("%.2f", productLine.getPrice()) + " x "
                    + df2.format(productLine.getQuantity()) + " = "
                    + String.format("%.2f", productLine.getTotal())).getBytes("GBK");
            getPrinter().write(ZJ58.ESC_Align_Right);
            getPrinter().write(byteArray);

            getPrinter().write(ZJ58.print_and_roll);

            total += productLine.getTotal();
        }

        // print total
        // 打印总计
        String totalString = "Total 总计： " + String.format("%.2f", total);
        getPrinter().write(totalString.getBytes("GBK"));
        getPrinter().write(ZJ58.print_and_roll);
    }

    void alignLeft() throws IOException {
        getPrinter().write(ZJ58.ESC_Align_Left);
    }

    void alignCenter() throws IOException {
        getPrinter().write(ZJ58.ESC_Align_Center);
    }

    void alignRight() throws IOException {
        getPrinter().write(ZJ58.ESC_Align_Right);
    }

    void bold() throws IOException {
        getPrinter().write(ZJ58.bold);
    }

    void cancel_bold() throws IOException {
        getPrinter().write(ZJ58.cancel_bold);
    }

    void font_size_double() throws IOException {
        getPrinter().write(ZJ58.font_size_double);
    }

    void font_size_normal() throws IOException {
        getPrinter().write(ZJ58.font_size_normal);
    }

    void printLine(String line) throws IOException {
        getPrinter().write(line.getBytes("GBK"));
        getPrinter().write(ZJ58.print_and_roll);
    }

    void printFooter() throws IOException {
        getPrinter().write(ZJ58.ESC_Align_Center);
        getPrinter().write("技术支持".getBytes("GBK"));
        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write("上海校欣信息技术有限公司".getBytes("GBK"));
//        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write(ZJ58.clean_end);
        getPrinter().write(ZJ58.cut);
        initializePrinter();
        // send all hex data stream
        // 发送所有16进制数据流
        getPrinter().flush();

        // close the stream
        // 关闭数据流
        //  getPrinter().close();
    }

    void printFooter(boolean printSupport) throws IOException {
        if(printSupport){
            getPrinter().write(ZJ58.ESC_Align_Center);
            getPrinter().write("技术支持".getBytes("GBK"));
            getPrinter().write(ZJ58.print_and_roll);
            getPrinter().write("上海校欣信息技术有限公司".getBytes("GBK"));
        }
//        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write(ZJ58.clean_end);
        getPrinter().write(ZJ58.cut);
        initializePrinter();
        // send all hex data stream
        // 发送所有16进制数据流
        getPrinter().flush();

        // close the stream
        // 关闭数据流
        //  getPrinter().close();
    }

    void printReceipt(String qrId) throws IOException {
        // get qr info
        QrReference qrRef = new QrReference(qrId);
        ArrayList<String> linesToPrint = qrRef.linesToPrint();
        if(linesToPrint == null){
            return;
        }
        // print logo
        // 打印公司图标
        printImage(CommonUtil.logoPath, 2);
        roll();

        font_size_double();
        bold();
        printLine("过磅单");
        cancel_bold();
        font_size_normal();
        roll();

        alignLeft();
        for(String line: linesToPrint){
            printLine(line);
        }

        // print qr code
        // 打印二维码
        int width = 350;
        int height = 350;
        String data = "qrID=" + qrId;
        try {
            byte[] qrCode = QrCode.getQRCodeImage(data, width, height);
            printImage(qrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        printFooter();
    }

}
