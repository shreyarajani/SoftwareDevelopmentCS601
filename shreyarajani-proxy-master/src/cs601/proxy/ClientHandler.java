package cs601.proxy;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ClientHandler implements Runnable {
    public static final int HTTP = 80;
    public static final boolean SingleThreaded = false;
    protected int port = HTTP;
    protected Socket socket;
    protected final Socket clientSocket;
    protected boolean debug = false;
    private DataInputStream browserIn;
    private DataOutputStream browserOut;
    private DataOutputStream serverOut;
    private DataInputStream serverIn;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            process();
        }
        catch (IOException e) {
            if(debug) {
                e.printStackTrace();
            }
        } finally {
            try {
                close();
                return;
            } catch (IOException e1) {
                if(debug) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /*
    Process all the functions. It starts from here
     */
    public void process() throws IOException {
        HashMap<String, String> header = processBrowserRequest();
        if (header == null) {
            return;
        }
        openUpstreamSocket(header.get("host"));
        makeUpstreamRequest(constructHeader(header));
        getUpStreamResponse();
    }

    /*
    Reads all the line from the browser. Typically, it will be headers from the request.
     */
    public HashMap<String, String> processBrowserRequest() throws IOException {
        browserIn = new DataInputStream(clientSocket.getInputStream());
        if(debug) {
            System.out.println("Processing browser request...");
        }
        String line = browserIn.readLine();
        if (line != null) {
            HashMap<String, String> headers = getHeaders(browserIn, line, true);
            if(debug) {
                System.out.println("Headers in processBrowserRequest: "+headers);
            }
            return headers;
        }
        return null;
    }

    /*
    gets the headers from the request sent by the browser
     */
    private HashMap<String, String> getHeaders(DataInputStream din, String line, boolean firstlineformat) throws IOException {
        HashMap<String, String> headersMap = new HashMap<String, String>();
        String[] split;
        String command = "";
        String URI = "";
        String HTTPv = "";
        if (firstlineformat) {
            split = line.split(" ");
            command = split[0];
            URI = split[1];
            String[] re = split[2].split("/");
            HTTPv = re[0] + "/1.0";
            headersMap.put("firstline", command + " " + HTTPv);
        } else {
            headersMap.put("firstline", line);
        }
        if(debug) {
            System.out.println("-----" + line);
        }
        line = din.readLine();
        while (line != null && line.length() != 0) {
            line = line.toLowerCase();
            if ((line.contains("connection") && line.contains("keep-alive")) || line.contains("user-agent") || line.contains("referer") || line.contains("proxy-connection")) {
                line = din.readLine();
                continue;
            }
            splitHeaders(line, firstlineformat, headersMap, command, URI, HTTPv);
            line = din.readLine();
        }
        if(debug) {
            System.out.println("========================");
            System.out.println("Headers in getHeaders: "+headersMap);
            System.out.println("========================");
        }
        return headersMap;
    }

    /*
    Splits the headers and saves it to the map
     */
    private void splitHeaders(String line, boolean firstlineformat, HashMap<String, String> headersMap, String command, String URI, String HTTPv) {
        String[] headerInfo = line.split(": ");
        headersMap.put(headerInfo[0], headerInfo[1]);
        if (firstlineformat && headerInfo[0].contains("host")) {
            String host = headerInfo[1];
            String[] ports = host.split(":");
            if(ports.length == 2){
                headersMap.put(headerInfo[0], ports[0]);
                port = Integer.parseInt(ports[1]);
            }
            String[] file = URI.split(host);
            if(file.length == 2) {
                String firstLine = command + " " + file[1] + " " + HTTPv;
                headersMap.put("firstline", firstLine);
            }
        }
    }

    /*
    Constructs the headers to forward it to the browser
     */
    public String constructHeader(HashMap<String, String> headers) {
        if(debug) {
            System.out.println("Constructing headers...");
        }
        String formatHeader = headers.get("firstline") + "\r\n";
        for (String key : headers.keySet()) {
            if (!key.equalsIgnoreCase("firstline")) {
                formatHeader += key + ": " + headers.get(key) + "\r\n";
            }
        }
        if(debug) {
            System.out.println("========================");
            System.out.println("Headers in constructHeader: "+headers);
            System.out.println("========================");
        }

        return formatHeader;
    }

    /*
    Opens a connection to the Server.
     */
    public void openUpstreamSocket(String host) {
        try {
            if(debug) {
                System.out.println("Connecting to the server...");

                System.out.println(host + " : " + port);
            }
            socket = new Socket(host, port);
            if(debug) {
                System.out.println("Connection successful at HOST: " + host + "and PORT: " + port);
            }
        } catch (IOException e) {
            if(debug) {
                System.out.print("Proxy server cannot connect to " + host + ":" + port);
                System.out.println(e);
            }
        }
    }

    /*
    Sends the request to the server.
     */
    public void makeUpstreamRequest(String headers) throws IOException {
        if(debug) {
            System.out.println("Sending request to server...");
        }
        serverOut = new DataOutputStream(socket.getOutputStream());
        serverOut.writeBytes(headers);
        serverOut.writeBytes("\r\n");
        serverOut.flush();
        if(debug) {
            System.out.println("Request sent to server");
        }
    }
    /*
    Read the response from the server
     */
    public void getUpStreamResponse() throws IOException {
        browserOut = new DataOutputStream(clientSocket.getOutputStream());
        if(debug) {
            System.out.println("Trying to get response from server...");
        }
        serverIn = new DataInputStream(socket.getInputStream());
        String line = "";
        if(debug) {
            System.out.println("Host --- " + socket.getPort() + " : " + socket.getInetAddress());
        }
        line = serverIn.readLine();
        if (line == null) {
            return;
        }
        HashMap<String, String> headers = getHeaders(serverIn, line, false);
        if(debug) {
            System.out.println("========================");
            System.out.println("Headers in getUpStreamResponse: "+headers);
            System.out.println("========================");
        }
        browserOut.write(constructHeader(headers).getBytes());
        browserOut.writeBytes("\r\n");
        checkContentLength(headers);
    }

    /*
    Checks for the content length
     */
    private void checkContentLength(HashMap<String, String> headers) throws IOException {
        int length = 0;
        if (headers.get("content-length") != null) {
            length = Integer.parseInt(headers.get("content-length"));
        }
        forwardServerDataToBrowser(length);
        browserOut.flush();
    }
    /*
    Sending the response to the browser that we received from the server
     */
    private void forwardServerDataToBrowser(int length) throws IOException {
        byte[] bytes = new byte[2048];
        int bytesGot;
        int totalValue = 0;
        if(length > 0) {
            while ((bytesGot = serverIn.read(bytes)) != -1) {
                if(totalValue < length) {
                    browserOut.write(bytes, 0, bytesGot);
                    totalValue = totalValue + bytesGot;
                }
            }
        }else{
            while ((bytesGot = serverIn.read(bytes)) != -1) {
                browserOut.write(bytes, 0, bytesGot);
                browserOut.flush();
            }
        }
    }

    /*
    Closes all the connections of the socket and the the Input and Output Streams
     */
    private void close() throws IOException {
        if (serverOut != null) {
            serverOut.flush();
            serverOut.close();
        }
        if (serverIn != null) {
            serverIn.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (browserIn != null) {
            browserIn.close();
        }
        if (browserOut != null) {
            browserOut.flush();
            browserOut.close();
        }
        if (clientSocket!= null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if(debug) {
            System.out.println("Connections closed at server socket: " + socket);
            System.out.println("Connections closed at client socket: " + clientSocket);
        }
    }
}