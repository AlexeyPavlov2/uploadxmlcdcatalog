package org.javatraining.uploadxmlcdcatalog.storage.fs.xml;

import org.javatraining.uploadxmlcdcatalog.model.ListMusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.util.List;

/**
 * Класс для записи XML-файла, содержащий записи о музыкальных дисках, класс MusicCompactDisk.
 * В реализации, в отличие от класса CatalogReaderFromXML, используется JAXB. Для разнобразия.
 *
 */

public class CatalogWriterToXML implements CatalogWriter<MusicCompactDisk> {
    private Logger logger = LoggerFactory.getLogger(CatalogWriterToXML.class);


    public synchronized void writeToOutputStream(List<MusicCompactDisk> list, OutputStream outputStream) {
        logger.info("Пишем список в OutpuStream");
        ListMusicCompactDisk disks = new ListMusicCompactDisk();
        disks.setDisks(list);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(ListMusicCompactDisk.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(disks, outputStream);
        } catch (JAXBException e) {
            logger.error("Ошибка во время записи XML-файла");
            e.printStackTrace();
        }

    }

}
