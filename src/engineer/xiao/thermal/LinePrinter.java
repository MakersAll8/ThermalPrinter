package engineer.xiao.thermal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

class LinePrinter {
    private String selectedLp;
    private ArrayList<String> availableLp = new ArrayList<>();
    PrintStream printStream = null;

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
            System.out.println("Available line printer devices: ");
            while((line = br.readLine()) != null){
                availableLp.add(line);
                System.out.println(line);
            }

            Scanner kb = new Scanner(System.in);
            do {
                // prompt user to choose a line printer
                // 提示用户选择打印机
                System.out.print("Choose your thermal printer: ");
                selectedLp = kb.nextLine();
            } while (!availableLp.contains(selectedLp));
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

    void printFooter() throws IOException {
        getPrinter().write(ZJ58.ESC_Align_Center);
        getPrinter().write("Tech Support 技术支持 ".getBytes("GBK"));
        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write("https://xiao.engineer".getBytes("GBK"));
        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write(ZJ58.print_and_roll);
        getPrinter().write(ZJ58.clean_end);

        // send all hex data stream
        // 发送所有16进制数据流
        getPrinter().flush();
        // close the stream
        // 关闭数据流
        getPrinter().close();
    }

}
