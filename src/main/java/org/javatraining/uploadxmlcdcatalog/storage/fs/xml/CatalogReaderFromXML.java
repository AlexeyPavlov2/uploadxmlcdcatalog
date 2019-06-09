package org.javatraining.uploadxmlcdcatalog.storage.fs.xml;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс для считывания XML-файла, содержащий записи о музыкальных дисках MusicCompactDisk.
 * Читает данные о компакт дисках из InputStream и сохраняет во внутреннем хранилище.
 *
 */

public class CatalogReaderFromXML implements CatalogReader<MusicCompactDisk> {
    private Logger logger = LoggerFactory.getLogger(CatalogReaderFromXML.class);

    /**
     * Метод возвращает список MusicCompactDisk из InputStream.
     *
     */

    public synchronized List<MusicCompactDisk> readFromInpuStream(InputStream inputStream) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(inputStream);
            return readDocument(reader);
        } catch (Exception e) {
            logger.error("Ошибка во время обработки XML-файла");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    logger.error("Ошибка во время обработки XML-файла");
                    e.printStackTrace();
                }
            }
        }
        return Collections.emptyList();
    }


    private List<MusicCompactDisk> readDocument(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.toLowerCase().equals("Catalog".toLowerCase()))
                        return readDisks(reader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }
        throw new XMLStreamException("Неожиданный конец файла");
    }

    private List<MusicCompactDisk> readDisks(XMLStreamReader reader) throws XMLStreamException {
        List<MusicCompactDisk> musicCompactDisks = new ArrayList<>();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.toLowerCase().equals("CD".toLowerCase()))
                        musicCompactDisks.add(readDisk(reader));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return musicCompactDisks;
            }
        }
        throw new XMLStreamException("Неожиданный конец файла");
    }

    private MusicCompactDisk readDisk(XMLStreamReader reader) throws XMLStreamException {
        MusicCompactDisk musicCompactDisk = new MusicCompactDisk();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.toLowerCase().equals("title".toLowerCase()))
                        musicCompactDisk.setTitle(readCharacters(reader));
                    else if (elementName.toLowerCase().equals("artist".toLowerCase()))
                        musicCompactDisk.setArtist(readCharacters(reader));
                    else if (elementName.toLowerCase().equals("country".toLowerCase()))
                        musicCompactDisk.setCountry(readCharacters(reader));
                    else if (elementName.toLowerCase().equals("company".toLowerCase()))
                        musicCompactDisk.setCompany(readCharacters(reader));
                    else if (elementName.toLowerCase().equals("price".toLowerCase()))
                        musicCompactDisk.setPrice(readBigDecimal(reader));
                    else if (elementName.toLowerCase().equals("year".toLowerCase()))
                        musicCompactDisk.setYear(readInt(reader));
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return musicCompactDisk;
            }
        }
        throw new XMLStreamException("Неожиданный конец файла");
    }

    private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Неожиданный конец файла");
    }

    private BigDecimal readBigDecimal(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return new BigDecimal(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Неверное значение BigDecimal " + characters);
        }
    }

    private int readInt(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Integer.valueOf(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Неверное значение Integer " + characters);
        }
    }
}
