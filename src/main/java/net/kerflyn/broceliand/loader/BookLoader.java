package net.kerflyn.broceliand.loader;

import com.google.common.base.Strings;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class BookLoader {

    public void parse(URL url) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        XMLStreamReader streamReader = null;
        try {
            streamReader = factory.createXMLStreamReader(inputStream);
            int level = 0;

            while (streamReader.hasNext()) {
                int eventType = streamReader.next();
                switch (eventType) {
                    case START_ELEMENT:
                        System.out.println(Strings.repeat("  ", level)
                                + streamReader.getName());
                        level++;
                        break;
                    case END_ELEMENT:
                        level--;
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        final String text = streamReader.getText().trim();
                        if (!text.isEmpty()) {
                            System.out.println(Strings.repeat(" ", level) + text);
                        }
                }
            }
        } catch (XMLStreamException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (streamReader != null) {
                    streamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
