package proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.util.Map;

public class MyHttpHandler implements HttpHandler {
    private Client client;

    public MyHttpHandler() {
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
        if (client == null) {
            client = new Client();
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        setHeaders(httpExchange);
        httpExchange.sendResponseHeaders(200, 0);
        try {
            Thread.sleep(10000);

        } catch (Exception e) {
        }
        new Thread(() -> {
            try {
                client.getInputStream().transferTo(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        outputStream.flush();
    }

    private void setHeaders(HttpExchange httpExchange) {
        Map<String, String> headers = client.getResponseHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getValue(), entry.getKey());
        }
    }
}