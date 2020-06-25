package engineer.xiao.thermal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class ServeHandler extends Thread {
    private final DataInputStream is;
    private final DataOutputStream os;
    private final Socket s;

    public ServeHandler(Socket s, DataInputStream is, DataOutputStream os) {
        this.s = s;
        this.is = is;
        this.os = os;
    }

    public void run() {
        String received;
        JsonObject failed = new JsonObject();
        failed.addProperty("error", "打印请求失败");

        JsonObject success = new JsonObject();
        success.addProperty("success", "打印请求已收到");

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));

        try {
            writer.write("{\"accept\":\"json\"}\n");
            writer.flush();
            received = reader.readLine();
            JsonElement input = JsonParser.parseString(received);
            if (input.isJsonObject()) {
                String qrId = input.getAsJsonObject().get("qrID").getAsString();
                ReceiptServer.lp.printReceipt(qrId);
                writer.write(success.toString());
                writer.flush();
                return;
            }
            writer.write(failed.toString());
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                writer.write(failed.toString());
                writer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
