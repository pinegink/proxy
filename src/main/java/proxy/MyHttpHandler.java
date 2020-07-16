package proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;

public class MyHttpHandler implements  HttpHandler {
    private Client client;
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String requestParamValue;
        requestParamValue = handleGetRequest(httpExchange);
        handleResponse(httpExchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        // return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        return "empty param";
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        if (client == null){
            client = new Client();
            new Thread(()-> client.start()).start();
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.getResponseHeaders().set("Content-Type","audio/mpeg");
        httpExchange.sendResponseHeaders(200, 0);
        try {
            Thread.sleep(10000);
        }catch (Exception e){};
        while (true){
            //System.out.println(new BufferedReader(new InputStreamReader(client.inputStream)).readLine());
//            outputStream.write(IOUtils.toByteArray(new BufferedInputStream(client.inputStream)));
//            outputStream.write(IOUtils.toByteArray(client.inputStream));
            System.out.println("hello");
            if (client.inputStream == null){
                System.out.println("null");
            }else{
                System.out.println("not null");
            }
            //outputStream.write(client.buf);
            client.inputStream.transferTo(outputStream);
            outputStream.flush();
        }
        //outputStream.close();
    }
}