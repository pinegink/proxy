package proxy.runnable;

import proxy.Client;

import java.io.IOException;

public class ReadToBuf implements Runnable {
    private byte[] buf;
    Client client;
    public ReadToBuf(byte[] buf, Client client){
        this.buf = buf;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (client.getStatus()) {
                client.getInputStream().read(buf);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
