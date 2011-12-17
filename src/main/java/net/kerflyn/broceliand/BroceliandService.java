package net.kerflyn.broceliand;

import com.google.common.io.Files;
import com.google.common.util.concurrent.AbstractService;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class BroceliandService extends AbstractService implements Container {

    private SocketConnection socketConnection;
    private int port;

    public BroceliandService(int port) {
        this.port = port;
    }

    @Override
    public void handle(Request request, Response response) {
        List<String> path = Arrays.asList(request.getPath().getSegments());

        try {
            String html = Files.toString(new File("public/index.html"), UTF_8);
            response.getPrintStream().append(html);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStart() {
        try {
            socketConnection = new SocketConnection(this);
            socketConnection.connect(new InetSocketAddress(port));
            notifyStarted();
        } catch (IOException e) {
            notifyFailed(e);
        }
    }

    @Override
    protected void doStop() {
        try {
            socketConnection.close();
            notifyStopped();
        } catch (IOException e) {
            notifyFailed(e);
        }
    }

    public static void main(String[] args) {
        new BroceliandService(8080).startAndWait();
    }

}
