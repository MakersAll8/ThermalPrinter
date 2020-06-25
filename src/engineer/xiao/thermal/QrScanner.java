package engineer.xiao.thermal;

import com.google.gson.JsonObject;

import java.util.Scanner;

public class QrScanner {
    private static Scanner kb = new Scanner(System.in);

    static String read(){
        System.out.print("扫描发货单二维码：");
        String decodedText;
        String qrId;

        // read qr scanner input
        while(true){
            if(kb.hasNextLine()){
                decodedText = kb.nextLine();
                break;
            }
        }

        // parse scanner input
        decodedText = decodedText.replaceAll("\\s", "");
        try {
            JsonObject jsonObject = new JsonObject();
            String[] splitComma = decodedText.split(",");
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
