package net.kerflyn.broceliand.loader;

import com.google.common.collect.Iterables;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class SellerLoader {

    /**
     * Get addresses by country, city, and seller.
     *
     * @param reader
     * @return
     */
    public Map<String, Map<String, Map<String, Set<String>>>> parse(Reader reader) {
        Map<String, Map<String, Map<String, Set<String>>>> result = newHashMap();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader streamReader = null;

        try {
            streamReader = factory.createXMLStreamReader(reader);
            LinkedList<String> currentPath = newLinkedList();

            while (streamReader.hasNext()) {
                int eventType = streamReader.next();
                switch (eventType) {
                    case START_ELEMENT:
                        if ("addresses".equals(streamReader.getName().toString())) {
                            Set<String> addresses = readBooks(streamReader);
                            addBooks(addresses, result, currentPath);
                        }
                        if (isInteresting(streamReader.getName())) {
                            currentPath.push(streamReader.getAttributeValue("", "name"));
                        }
                        break;
                    case END_ELEMENT:
                        if (isInteresting(streamReader.getName())) {
                            currentPath.pop();
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (streamReader != null) {
                    streamReader.close();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    private void addBooks(Set<String> books, Map<String, Map<String, Map<String, Set<String>>>> storage, List<String> path) {
        createPath(storage, path);
        storage.get(path.get(2)).get(path.get(1)).get(path.get(0)).addAll(books);
    }

    private void createPath(Map<String, Map<String, Map<String, Set<String>>>> storage, List<String> path) {
        String country = path.get(2);
        if (!storage.containsKey(country)) {
            storage.put(country, new HashMap<String, Map<String, Set<String>>>());
        }

        Map<String, Map<String, Set<String>>> cities = storage.get(country);
        String city = path.get(1);
        if (!cities.containsKey(city)) {
            cities.put(city, new HashMap<String, Set<String>>());
        }

        Map<String, Set<String>> sellers = cities.get(city);
        String seller = path.get(0);
        if (!sellers.containsKey(seller)) {
            sellers.put(seller, new HashSet<String>());
        }
    }

    private Set<String> readBooks(XMLStreamReader streamReader) throws XMLStreamException {
        Set<String> addresses = newHashSet();

        int eventType = streamReader.nextTag();
        while (eventType != END_ELEMENT && !"addresses".equals(streamReader.getName().toString())) {
            if (eventType == START_ELEMENT && "address".equals(streamReader.getName().toString())) {
                String address = readBook(streamReader);
                addresses.add(address);
            }
            eventType = streamReader.nextTag();
        }

        return addresses;
    }

    private String readBook(XMLStreamReader streamReader) throws XMLStreamException {
        streamReader.next(); // CHARACTER: address
        String address = streamReader.getText().trim();
        streamReader.next(); // END_ELEMENT: address

        return address;
    }

    private boolean isInteresting(QName name) {
        return Iterables.contains(Arrays.asList("country", "city", "seller"), name.toString());
    }
}
