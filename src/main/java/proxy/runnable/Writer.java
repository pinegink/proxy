package proxy.runnable;

import proxy.Handler;

import java.io.IOException;
import java.io.OutputStream;

public class Writer implements Runnable {
    Handler handler;
    private OutputStream outputStream;
    private byte[] buf;
    private Integer clients;
    public Writer(OutputStream outputStream, byte[] buf, Handler handler) {
        this.outputStream = outputStream;
        this.buf = buf;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            byte old = 1;
            while (true) {
                if (buf[0] != old) {
                    old = buf[0];
                    outputStream.write(buf);
                    outputStream.flush();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            handler.decrClients();
            e.printStackTrace();
        }
    }
}
