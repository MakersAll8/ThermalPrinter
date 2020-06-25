package engineer.xiao.thermal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonUtil {
    static String logoPath;

    public static void loadProperties(String propertiesPath){
        try {
            InputStream input = new FileInputStream(propertiesPath);
            Properties prop = new Properties();
            prop.load(input);

            logoPath = prop.getProperty("logoPath");
            XHR.apiServer = prop.getProperty("apiServer");
            XHR.authKey = prop.getProperty("authKey");
            LinePrinter.lpId = prop.getProperty("lpId", "lp0");
            ReceiptServer.serverPort = Integer.parseInt(
                    prop.getProperty("serverPort", "5888")
            );

        } catch (FileNotFoundException e) {
            System.out.println("没找到配置文档config.properties");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("配置文档config.properties读取错误");
            System.exit(1);
        }
    }
}
