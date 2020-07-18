package proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Logger;

public class MyHttpHandler implements HttpHandler {
    private static Logger log = Logger.getLogger(MyHttpHandler.class.getName());
    private Client client;
    private byte[] buf;
    private int clients;

    public MyHttpHandler() {
        System.out.println("httpHandler created");
        buf = new byte[16384];
        clients = 0;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("handle");
        String requestParamValue;
        requestParamValue = handleGetRequest(httpExchange);
        handleResponse(httpExchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        // return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        return "empty param";
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        clients++;
        if (client == null) {
            client = new Client();
            new Thread(() -> {
                while (client!=null) {
                    System.out.println("clients:" + clients);
                    if (clients == 0) {
                        client.closeConnection(); // закрывается, проверено
                        client = null;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            new Thread(() -> {
                try {
                    while (client != null) {
                        client.getInputStream().read(buf);
//                        bufferedInputStream.read(buf);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        setHeaders(httpExchange);
        httpExchange.sendResponseHeaders(200, 0);

//            BufferedInputStream bufferedInputStream = new BufferedInputStream(client.getInputStream());


        new Thread(() -> {
            try {
                byte old = 0;
                while (true) {
                    if (buf[0] != old) {
                        old = buf[0];
                        outputStream.write(buf);
                        outputStream.flush();
                        System.out.println((int) buf[0]);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                clients--;
                e.printStackTrace();
            }

        }).start();
    }

    private void setHeaders(HttpExchange httpExchange) {
        Map<String, String> headers = client.getResponseHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getValue(), entry.getKey());
        }
    }
}