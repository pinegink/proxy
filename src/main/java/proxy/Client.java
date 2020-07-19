package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Client {
    private static Logger log = Logger.getLogger(Client.class.getName());
    private URL url;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private Map<String, String> responseHeaders;
    private boolean status;

    public Client(String url) throws IOException {
        this.url = new URL(url);
        status = true;
        start();
    }

    public void start() throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        setHeaders();
        inputStream = connection.getInputStream();
    }

    public void shutDown() {
        status = false;
        connection.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public boolean getStatus() {
        return status;
    }

    private void setHeaders() {
        responseHeaders = new HashMap<>();
        int i = 1;
        while (connection.getHeaderField(i) != null) {
            String field = connection.getHeaderField(i);
            String key = connection.getHeaderFieldKey(i);
            responseHeaders.put(field, key);
            i++;
        }
    }

}
