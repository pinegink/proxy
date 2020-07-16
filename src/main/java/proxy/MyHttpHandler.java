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
        httpExchange.getResponseHeaders().set("icy-br","128");
        httpExchange.getResponseHeaders().set("icy-pub","1");
        httpExchange.getResponseHeaders().set("icy-description","Flux 70er");
        httpExchange.getResponseHeaders().set("icy-audio-info","channels=2;samplerate=44100;bitrate=128");
        httpExchange.sendResponseHeaders(200, 0);
        try {
            Thread.sleep(10000);
        }catch (Exception e){}
        new Thread(()-> {
            try {
                outputStream.write(IOUtils.toByteArray(new BufferedInputStream(this.client.inputStream)));
            } catch (IOException e) {}
        }).start();
        while (true){
            System.out.println(new BufferedReader(new InputStreamReader(this.client.inputStream)).readLine());


//            outputStream.write(IOUtils.toByteArray(client.inputStream));
            if (client.inputStream == null){
                System.out.println("null");
            }else{
                System.out.println("not null");
            }
            //outputStream.write(client.buf);
            //this.client.inputStream.transferTo(outputStream);
            outputStream.flush();
        }
        //outputStream.close();
    }
}