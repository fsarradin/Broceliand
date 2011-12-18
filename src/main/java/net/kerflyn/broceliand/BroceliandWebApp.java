package net.kerflyn.broceliand;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractService;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.impl.BookRepositoryImpl;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.impl.BookServiceImpl;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.skip;
import static com.google.inject.name.Names.named;
import static java.util.Arrays.asList;
import static net.kerflyn.broceliand.util.Reflections.invoke;

public class BroceliandWebApp extends AbstractService implements Container {

    private SocketConnection socketConnection;
    private int port;
    public Injector injector;

    public BroceliandWebApp(int port) {
        this.port = port;

        injector = Guice.createInjector(new JpaPersistModule("hsqldb-pu"),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(BookRepository.class).to(BookRepositoryImpl.class);
                        bind(BookService.class).to(BookServiceImpl.class);
                        bind(Key.get(Object.class, named("index"))).to(IndexController.class);
                    }
                });
        injector.getInstance(Initializer.class);
    }

    @Override
    public void handle(Request request, Response response) {
        List<String> path = asList(request.getPath().getSegments());
        String action = getFirst(path, "index");
        try {
            invoke(getController(action), "render", getArguments(response, path));
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Object> getArguments(Response response, List<String> path) {
        return ImmutableList.builder().add(response).addAll(skip(path, 1)).build();
    }

    private Object getController(String action) {
        return injector.getInstance(Key.get(Object.class, Names.named(action)));
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
        new BroceliandWebApp(8080).startAndWait();
    }

    private static class Initializer {
        @Inject
        public Initializer(PersistService service) {
            service.start();
        }
    }
}
