package proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private InputStream inputStream;
    private Map<String, String> responseHeaders;

    public void start() {
        try {
            URL url = new URL("http://fluxfm.hoerradar.de/fluxfm-berlin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            setHeaders(connection);
            this.inputStream = connection.getInputStream();
            System.out.println("passed through GET");
            //connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        if (inputStream == null){
            start();
        }
        return inputStream;
    }

    public Map<String, String> getResponseHeaders() {
        if (responseHeaders == null){
            start();
        }
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
