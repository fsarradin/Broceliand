/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
