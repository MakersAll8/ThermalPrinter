package engineer.xiao.thermal;

import com.google.zxing.WriterException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.*;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.Level.OFF;

public class Main implements NativeKeyListener {
    private static final int[] SHIFT_RAW_CODE = new int[]{65505, 65506};
    private static final int[] ENTER_RAW_CODE = new int[]{65421, 65293};
    private static String scanResult = null;
    private static StringBuilder stringBuilder = new StringBuilder();
    private static LinePrinter lp = null;

    private static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
//        System.out.println("--------------------");
//        System.out.println("Char: " + nativeKeyEvent.getKeyChar());
//        System.out.println("RawC: " + nativeKeyEvent.getRawCode());
        int rawCode = nativeKeyEvent.getRawCode();
        if(!contains(SHIFT_RAW_CODE, rawCode)
                && !contains(ENTER_RAW_CODE, rawCode)){
            stringBuilder.append(nativeKeyEvent.getKeyChar());
        }

        if(contains(ENTER_RAW_CODE, rawCode)){
            scanResult = stringBuilder.toString();
            stringBuilder.delete(0, stringBuilder.length());
            System.out.println(scanResult);
            String qrID = QrScanner.decodeScan(scanResult);
            try {
                lp.printReceipt(qrID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanResult = null;
        }

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }


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
        lp = new LinePrinter();
        ReceiptServer.lp = lp;

        // start the print receipt request server
        ReceiptServer server = new ReceiptServer();
        server.start();

//        do {
//            // wait for qr code scan
//            String qrId = QrScanner.read();
//            // print the qr id
//            lp.printReceipt(qrId);
//        } while (true);

        // Clear previous logging configurations.
        LogManager.getLogManager().reset();
        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(OFF);
        logger.setUseParentHandlers(false);


        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        GlobalScreen.addNativeKeyListener(new Main());

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