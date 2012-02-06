package net.kerflyn.broceliand.loader;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class BookLoaderTest {

    private StringReader reader;

    @Before
    public void setUp() throws Exception {
        String content = "<?xml version='1.0' encoding='UTF-8'?>" +
                "<catalog>" +
                "<country name='A Country'><city name='A City'><seller name='A Seller'><books><book>" +
                "<title>A Title</title><author>An Author</author><price>1.00</price>" +
                "</book></books></seller></city></country>" +
                "</catalog>";
        reader = new StringReader(content);
    }

    @Test
    public void should_load_books() throws Exception {
        BookLoader loader = new BookLoader();

        Map<String, Map<String, Map<String, Set<BookObject>>>> countries = loader.parse(reader);
        assertThat(countries.containsKey("A Country")).isTrue();
        
        Map<String, Map<String, Set<BookObject>>> cities = countries.get("A Country");
        assertThat(cities.containsKey("A City")).isTrue();
        
        Map<String, Set<BookObject>> sellers = cities.get("A City");
        assertThat(sellers.containsKey("A Seller")).isTrue();

        Set<BookObject> books = sellers.get("A Seller");
        assertThat(books).hasSize(1);
        assertThat(books).onProperty("title").isEqualTo(Arrays.asList("A Title"));
        assertThat(books).onProperty("price").isEqualTo(Arrays.asList(new BigDecimal("1.00")));
    }

}
