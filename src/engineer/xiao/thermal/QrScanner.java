package engineer.xiao.thermal;

import com.google.gson.JsonObject;

import java.util.Scanner;

public class QrScanner {
    private static Scanner kb = new Scanner(System.in);

    static String read(){
        System.out.print("扫描发货单二维码：");
        String decodedText;

        // read qr scanner input
        while(true){
            if(kb.hasNextLine()){
                decodedText = kb.nextLine();
                break;
            }
        }

        return decodeScan(decodedText);

    }

    static String decodeScan(String scanResult){
        String qrId;
        // parse scanner input
        scanResult = scanResult.replaceAll("\\s", "");
        try {
            JsonObject jsonObject = new JsonObject();
            String[] splitComma = scanResult.split(",");
            for (String item : splitComma) {
                String[] keyValuePair = item.split("=");
                jsonObject.addProperty(keyValuePair[0], keyValuePair[1]);
            }
            qrId = jsonObject.get("qrID").getAsString();
        } catch (Exception e) {
            return null;
        }
        return qrId;
    }
}
