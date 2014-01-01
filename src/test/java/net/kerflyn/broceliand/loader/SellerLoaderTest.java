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

package net.kerflyn.broceliand.loader;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;


public class SellerLoaderTest {

    private StringReader reader;

    @Before
    public void setUp() throws Exception {
        String content = "<?xml version='1.0' encoding='UTF-8'?>" +
                "<catalog>" +
                "<country name='A Country'><city name='A City'><seller name='A Seller'><addresses>" +
                "<address>aaa</address>" +
                "</addresses></seller></city></country>" +
                "</catalog>";
        reader = new StringReader(content);
    }

    @Test
    public void should_load_books() throws Exception {
        SellerLoader loader = new SellerLoader();

        Map<String, Map<String, Map<String, Set<String>>>> countries = loader.parse(reader);
        assertThat(countries.containsKey("A Country")).isTrue();
        
        Map<String, Map<String, Set<String>>> cities = countries.get("A Country");
        assertThat(cities.containsKey("A City")).isTrue();
        
        Map<String, Set<String>> sellers = cities.get("A City");
        assertThat(sellers.containsKey("A Seller")).isTrue();

        Set<String> addresses = sellers.get("A Seller");
        assertThat(addresses).containsOnly("aaa");
    }

}
