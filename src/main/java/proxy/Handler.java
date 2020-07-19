package proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import proxy.runnable.Monitoring;
import proxy.runnable.ReadToBuf;
import proxy.runnable.Writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Handler implements HttpHandler {
    private static Logger log = Logger.getLogger(Handler.class.getName());
    private Client client;
    private byte[] buf;
    private Integer clients = 0;
    private String url;
    private Map<String, String> urls;

    public Handler() {
        buf = new byte[16384];
        urls = new HashMap<>();
        urls.put("/fluxfm-berlin", "http://fluxfm.hoerradar.de/fluxfm-berlin");
        urls.put("/flux-70er-mp3-mq", "http://fluxfm.hoerradar.de/flux-70er-mp3-mq");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        synchronized (this){
            clients++;
        }
        url = urls.get(httpExchange.getRequestURI().toString());
        synchronized (this){
            if (client == null) {
                startClient();
            }else if (!client.getStatus()){
                startClient();
            }
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        setHeaders(httpExchange);
        httpExchange.sendResponseHeaders(200, 0);
        Runnable writer = new Writer(outputStream, buf, this);
        new Thread(writer).start();
    }
    public Client getClient(){
        return client;
    }
    public synchronized void decrClients (){
        clients--;
    }
    public int getClients (){
        return clients;
    }
    private void startClient() throws IOException {
        client = new Client(url);
        Runnable monitoring = new Monitoring(this);
        Runnable readToBuf = new ReadToBuf(buf, client);
        new Thread(monitoring).start();
        new Thread(readToBuf).start();
    }

    private void setHeaders(HttpExchange httpExchange) {
        Map<String, String> headers = client.getResponseHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpExchange.getResponseHeaders().set(entry.getValue(), entry.getKey());
        }
    }
}