package engineer.xiao.thermal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiptServer extends Thread {
    static int serverPort;
    static LinePrinter lp;

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket s = serverSocket.accept();
                Thread serve = new ServeHandler(s);
                serve.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
