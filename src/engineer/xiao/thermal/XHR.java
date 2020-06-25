package engineer.xiao.thermal;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class XHR {
    static String authKey = "";
    static String apiServer;

    static JsonElement get(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("AUTH", authKey);
            con.setDoOutput(true);

            return getResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static JsonElement post(String urlString, JsonElement jsonElement) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("AUTH", authKey);
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonElement.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            return getResponse(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JsonElement getResponse(HttpURLConnection con){
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return JsonParser.parseString(response.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
