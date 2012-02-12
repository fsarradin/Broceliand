package net.kerflyn.broceliand.loader;

import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class SellerAccessorTest {

    private Map<String, Map<String, Map<String, Set<String>>>> catalog;

    private SellerAccessor sellerAccessor;

    @Before
    public void setUp() throws Exception {
        catalog = getCatalog("loader/sellers.xml");
        sellerAccessor = new SellerAccessor(catalog);
    }

    @Test
    public void should_get_a_book_set() {
        Set<String> addresses = sellerAccessor.get("UK", "London", "C");

        assertThat(addresses).isNotNull();
        assertThat(addresses).hasSize(2);
    }

    @Test
    public void should_get_a_book_set_with_an_unknown_city() {
        Set<String> addresses = sellerAccessor.get("UK", "Atlantis", "C");

        assertThat(addresses).isNull();
    }

    private static Map<String, Map<String, Map<String, Set<String>>>> getCatalog(String resourceName) throws IOException {
        SellerLoader sellerLoader = new SellerLoader();
        URL url = Resources.getResource(resourceName);
        Reader reader = new InputStreamReader(url.openStream());
        try {
            return sellerLoader.parse(reader);
        } finally {
            reader.close();
        }
    }

}
