package cs601.proxy;

import java.io.*;
import java.net.*;

public class ProxyServer {
    public static void main(String[] args) throws IOException {

        int port = 8080;
        if(args.length == 1) {
            port = Integer.valueOf(args[0]);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Proxy Started at port "+port);

        while (true){
            Socket socket = serverSocket.accept();
            socket.setSoTimeout(10000);
            ClientHandler clientHandler=new ClientHandler(socket);
            Thread t1 = new Thread(clientHandler);
            t1.start();
        }
    }
}