package net.kerflyn.broceliand;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.kerflyn.broceliand.configuration.BroceliandConfiguration;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Templates;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.skip;
import static java.util.Arrays.asList;
import static net.kerflyn.broceliand.util.Reflections.invoke;

public class BroceliandWebApp extends AbstractService implements Container {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroceliandWebApp.class);

    private SocketConnection socketConnection;
    private int port;
    public Injector injector;

    public BroceliandWebApp(int port) {
        this.port = port;
        this.injector = BroceliandConfiguration.newGuiceInjector();
        bootstrap();
    }

    private void bootstrap() {
        UserService userService = injector.getInstance(UserService.class);
        Users.checkForAdminAccount(userService);
    }

    @Override
    public void handle(Request request, Response response) {
        List<String> path = asList(request.getPath().getSegments());
        String action = getFirst(path, "index");
        try {
            LOGGER.info("action: " + action + ", path: " + path);
            invoke(getController(action), "render", getArguments(request, response, path));
        } catch (Throwable e) {
            LOGGER.error("Error during invocation", e);
            final Status status = Status.INTERNAL_SERVER_ERROR;
            response.setCode(status.getCode());
            URL groupUrl = null;
            try {
                groupUrl = new File("template/error.stg").toURI().toURL();
                ST template = Templates.buildTemplate(groupUrl);
                template.addAggr("metadata.{title, code}",
                        new Object[] { status.getDescription(), status.getCode() });
                template.addAggr("data.{stackTrace}",
                        new Object[] { Throwables.getStackTraceAsString(e) });
                response.getPrintStream().append(template.render());
            } catch (IOException e1) {
                LOGGER.error("Error while getting error page", e1);
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else  if (e instanceof Error) {
                throw (Error) e;
            }
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Object> getArguments(Request request, Response response, List<String> path) {
        return ImmutableList.builder()
                .add(request).add(response)
                .addAll(skip(path, 1))
                .build();
    }

    private Object getController(String action) {
        return injector.getInstance(Key.get(Object.class, Names.named(action)));
    }

    @Override
    protected void doStart() {
        try {
            Server server = new ContainerServer(this, 1);
            socketConnection = new SocketConnection(server);
            socketConnection.connect(new InetSocketAddress(port));
            notifyStarted();
            LOGGER.info("service running");
        } catch (IOException e) {
            notifyFailed(e);
        }
    }

    @Override
    protected void doStop() {
        try {
            socketConnection.close();
            notifyStopped();
            LOGGER.info("service stopped");
        } catch (IOException e) {
            notifyFailed(e);
        }
    }

    public static void main(String[] args) {
        final BroceliandWebApp application = new BroceliandWebApp(8080);
        application.startAndWait();
    }

}
