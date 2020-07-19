package proxy;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            HttpContext context1 = server.createContext("/fluxfm-berlin");
            HttpContext context2 = server.createContext("/flux-70er-mp3-mq");
            context1.setHandler(new Handler());
            context2.setHandler(new Handler());
            server.setExecutor(threadPoolExecutor);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
