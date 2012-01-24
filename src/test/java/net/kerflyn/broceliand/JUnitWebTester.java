package net.kerflyn.broceliand;

import ch.qos.logback.classic.LoggerContext;
import com.google.common.util.concurrent.Service;
import com.google.inject.Injector;
import net.kerflyn.broceliand.test.configuration.BroceliandTestConfiguration;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static ch.qos.logback.classic.Level.DEBUG;

public class JUnitWebTester extends WebTester {

    private static final Random RANDOM = new Random();
    private static final int BASE_PORT = 8642;

    private Service service;
    private int port;

    public JUnitWebTester() {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("ROOT").setLevel(DEBUG);
    }

    @Before
    public void startService() {
        for (int i = 0; i < 50; i++) {
            try {
                port = getRandomPort();

                final Injector injector = BroceliandTestConfiguration.newGuiceInjector();
                service = new BroceliandWebApp(port, injector);
                service.startAndWait();

                setBaseUrl("http://localhost:" + port);
                return;
            } catch (Exception e) {
                System.err.println("Unable to bind server: " + e.getMessage());
            }
        }
        throw new IllegalStateException("Unable to start server");
    }

    private int getRandomPort() {
        synchronized (RANDOM) {
            return RANDOM.nextInt(1000) + BASE_PORT;
        }
    }

    @After
    public void stopService() {
        if (service != null) {
            service.stopAndWait();
        }
    }

}
