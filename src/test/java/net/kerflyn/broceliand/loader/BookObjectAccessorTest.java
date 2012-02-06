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

public class BookObjectAccessorTest {

    private Map<String, Map<String, Map<String, Set<BookObject>>>> catalog;

    private BookObjectAccessor bookAccessor;

    @Before
    public void setUp() throws Exception {
        catalog = getCatalog("loader/books.xml");
        bookAccessor = new BookObjectAccessor(catalog);
    }

    @Test
    public void should_get_a_book_set() {
        Set<BookObject> books = bookAccessor.get("UK", "London", "C");

        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
    }

    @Test
    public void should_get_a_book_set_with_an_unknown_city() {
        Set<BookObject> books = bookAccessor.get("UK", "Atlantis", "C");

        assertThat(books).isNull();
    }

    private static Map<String, Map<String, Map<String, Set<BookObject>>>> getCatalog(String resourceName) throws IOException {
        BookLoader bookLoader = new BookLoader();
        URL url = Resources.getResource(resourceName);
        Reader reader = new InputStreamReader(url.openStream());
        try {
            return bookLoader.parse(reader);
        } finally {
            reader.close();
        }
    }

}
