package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Client {
    private static Logger log = Logger.getLogger(Client.class.getName());
    private InputStream inputStream;
    private Map<String, String> responseHeaders;
    private HttpURLConnection connection;
    public Client(){
        System.out.println("client created");
        start();
    }

    public void start() {
        try {
            URL url = new URL("http://fluxfm.hoerradar.de/flux-70er-mp3-mq");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            this.connection = connection;
            setHeaders(connection);
            this.inputStream = connection.getInputStream();
            System.out.println("client started");
            //connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeConnection(){
        connection.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }
    private void setHeaders (HttpURLConnection connection) {
        responseHeaders = new HashMap<>();
        int i = 1;
        while (connection.getHeaderField(i)!=null){
            String field = connection.getHeaderField(i);
            String key = connection.getHeaderFieldKey(i);
            responseHeaders.put(field, key);
            i++;
        }
    }
}
