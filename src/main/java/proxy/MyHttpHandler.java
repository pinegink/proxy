package proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyHttpHandler implements HttpHandler {
    private Client client;
    private ByteBuffer byteBuffer;
    private List<ByteBuffer> duplicateBuf;
    private int clientId;
    byte[] buf;

    public MyHttpHandler() {
        this.duplicateBuf = new ArrayList<>();
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

        if (buf == null){
            buf = new byte[16384];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(client.getInputStream());
            new Thread(()->{
                try {
                    while (true){
//                        client.getInputStream().read(buf);
                        bufferedInputStream.read(buf);
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
        new Thread(()->{
            try {
                byte old =0;
                while (true){
                    if (buf[0] != old){      /// если поставить такой if - данные в buf вообще не обновляются
                        old = buf[0];
                        outputStream.write(buf);
                        outputStream.flush();
                        System.out.println((int)buf[0]);
                    }
                    /////////     Видимо, Thread пишет в outputstrean слишком быстро и пытается писать раньше, чем
                    ////////      stream освободится - sleep (1) проблему с исключением решает
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


/////////////////////////////////////////////////////////////////////////////////////////////////////
//           Пробовал через byteBuffer - игнорируйте
/////////////////////////////////////////////////////////////////////////////////////////////////////
//        try {
//            Thread.sleep(10000);
//
//        } catch (Exception e) {
//        }
//        if (this.byteBuffer == null) {
//            new Thread(() -> {
//                try {
//                    this.byteBuffer = ByteBuffer.wrap(client.getInputStream().readAllBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//        new Thread(() -> duplicateBuf.add(this.byteBuffer.duplicate())).start();
//        WritableByteChannel channel = Channels.newChannel(outputStream);
//
//        new Thread(() -> {
//            try {
//                channel.write(duplicateBuf.get(clientId));
//                clientId++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
/////////////////////////////////////////////////////////////////////////////////////////////////////




/////////////////////////////////////////////////////////////////////////////////////////////////////
//             Вот так работает, но только с одним потоком
/////////////////////////////////////////////////////////////////////////////////////////////////////
//        new Thread(() -> {   //
//            try {
//                client.getInputStream().transferTo(outputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        outputStream.flush();
/////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    private void setHeaders(HttpExchange httpExchange) {
        Map<String, String> headers = client.getResponseHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getValue(), entry.getKey());
        }
    }
}