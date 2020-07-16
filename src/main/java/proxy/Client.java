package proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {
    public InputStream inputStream;

    public void start() {
        try {
            URL url = new URL("http://fluxfm.hoerradar.de/fluxfm-berlin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            this.inputStream = connection.getInputStream();
            System.out.println("passed through GET");
//            while (true) {
//                System.out.println(new BufferedReader(new InputStreamReader(inputStream)).readLine());
//                buf = new BufferedReader(new InputStreamReader(inputStream)).readLine().getBytes();
//                Thread.sleep(1000);
//            }
            //connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
