package net.kerflyn.broceliand;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Injector;
import net.kerflyn.broceliand.configuration.BroceliandConfiguration;
import net.kerflyn.broceliand.route.Routes;
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

public class BroceliandWebApp extends AbstractService implements Container {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroceliandWebApp.class);

    private SocketConnection socketConnection;

    /** Connection port. */
    private int port;

    private Injector injector;

    private Routes routes;

    public BroceliandWebApp(int port, Injector injector) {
        this.port = port;
        this.injector = injector;
        this.routes = this.injector.getInstance(Routes.class);

        bootstrap();
    }

    private void bootstrap() {
        UserService userService = injector.getInstance(UserService.class);

        Users.autoCreateAdministratorAccount(userService);
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            routes.handle(request, response, injector);
        } catch (Throwable e) {
            LOGGER.error("Error while routing", e);
            final Status status = Status.INTERNAL_SERVER_ERROR;
            response.setCode(status.getCode());
            try {
                ST template = buildErrorTemplate(e, status);
                response.getPrintStream().append(template.render());
            } catch (IOException e1) {
                LOGGER.error("Error while getting error page", e1);
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            }
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing response stream", e);
            }
        }
    }

    private ST buildErrorTemplate(Throwable e, Status status) throws MalformedURLException {
        URL groupUrl = new File("template/error.stg").toURI().toURL();
        ST template = Templates.buildTemplate(groupUrl);
        template.addAggr("metadata.{title, code}",
                new Object[] { status.getDescription(), status.getCode() });
        template.addAggr("data.{stackTrace}",
                new Object[] { Throwables.getStackTraceAsString(e) });
        return template;
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
        final Injector injector = BroceliandConfiguration.newGuiceInjector();
        final BroceliandWebApp application = new BroceliandWebApp(8080, injector);

        application.startAndWait();
    }

}
