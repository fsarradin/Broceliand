package net.kerflyn.broceliand;

import ch.qos.logback.classic.LoggerContext;
import com.google.common.util.concurrent.Service;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;

public class BroceliandTest extends WebTester {

    private static final Random RANDOM = new Random();
    private static final int BASE_PORT = 8642;

    private Service service;
    private int port;

    public BroceliandTest() {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("ROOT").setLevel(DEBUG);
    }

    @Before
    public void startService() {
        for (int i = 0; i < 50; i++) {
            try {
                port = getRandomPort();

                service = new BroceliandService(port);
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

    @Test
    public void shouldSayHello() {
        beginAt("/");
        assertTextPresent("Broceliand");
    }

}
