package proxy.runnable;

import proxy.Client;
import proxy.Handler;

public class Monitoring implements Runnable {
    Client client;
    Handler handler;

    public Monitoring( Handler handler) {
        client = handler.getClient();
        this.handler = handler;
    }

    @Override
    public void run() {
        while (client.getStatus()) {
            if (handler.getClients() == 0 && client.getStatus()) {
                client.shutDown();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
