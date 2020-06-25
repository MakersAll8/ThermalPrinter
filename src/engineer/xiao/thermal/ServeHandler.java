package engineer.xiao.thermal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class ServeHandler extends Thread {

    private final Socket s;
    private DataInputStream is;
    private DataOutputStream os;

    public ServeHandler(Socket s) throws IOException {
        this.s = s;
        is = new DataInputStream(s.getInputStream());
        os = new DataOutputStream(s.getOutputStream());
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
            received = reader.readLine();
            JsonElement input = JsonParser.parseString(received);
            if (input.isJsonObject()) {
                String qrId = input.getAsJsonObject().get("qrID").getAsString();
                ReceiptServer.lp.printReceipt(qrId);
                writer.write(success.toString()+"\n");
                writer.flush();
            } else {
                writer.write(failed.toString()+"\n");
                writer.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                is.close();
                os.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
