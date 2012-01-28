package net.kerflyn.broceliand.loader;

import com.google.common.io.Resources;
import org.junit.Test;

import java.net.URL;

public class BookLoaderTest {

    @Test
    public void should_load_books() {
        BookLoader loader = new BookLoader();

        URL fileUrl = Resources.getResource("loader/books.xml");
        loader.parse(fileUrl);
    }

}
