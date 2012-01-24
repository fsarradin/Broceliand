package net.kerflyn.broceliand;

import org.junit.Test;

public class BroceliandTest extends JUnitWebTester {

    @Test
    public void shouldSayHello() {
        beginAt("/");
        assertTextPresent("Broceliand");
        assertTextPresent("login");
    }

    @Test
    public void shouldDisplayLoginForm() {
        beginAt("/user/login");
        assertFormPresent();
        assertTextPresent("Login");
        assertTextPresent("Password");
        assertSubmitButtonPresent();
    }

}
