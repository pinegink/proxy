package proxy.streamMultiplex;

import java.io.*;
import java.util.*;

public class MultiplexOutputStream extends OutputStream {
    private Vector m_streams;

    public MultiplexOutputStream() {
    }

    public MultiplexOutputStream(OutputStream os) {
        addOutputStream(os);
    }

    public Enumeration getOutputStreams() {
        if (m_streams != null)
            return m_streams.elements();
        return null;
    }
    public void addOutputStream(OutputStream os) {
        if (os == null)
            return;
        if (m_streams == null)
            m_streams = new Vector();
        m_streams.addElement(os);
    }
    public void removeOutputStream(OutputStream os) {
        if (m_streams != null && os != null)
            m_streams.removeElement(os);
    }

    public void close() throws IOException {
        Enumeration e = getOutputStreams();
        if (e == null)
            return;
        IOException ioe = null;
        while (e.hasMoreElements()) {
            try {
                ((OutputStream) e.nextElement()).close();
            } catch (IOException exc) {
                ioe = exc;
            }
        }
        if (ioe != null)
            throw ioe;
    }

    public void flush() throws IOException {
        Enumeration e = getOutputStreams();
        if (e == null)
            return;
        IOException ioe = null;
        while (e.hasMoreElements()) {
            try {
                ((OutputStream) e.nextElement()).flush();
            } catch (IOException exc) {
                ioe = exc;
            }
        }
        if (ioe != null)
            throw ioe;
    }

    public void write(int b) throws IOException {
        Enumeration e = getOutputStreams();
        if (e == null)
            return;
        IOException ioe = null;
        while (e.hasMoreElements()) {
            try {
                ((OutputStream) e.nextElement()).write(b);
            } catch (IOException exc) {
                ioe = exc;
            }
        }
        if (ioe != null)
            throw ioe;
    }
}
