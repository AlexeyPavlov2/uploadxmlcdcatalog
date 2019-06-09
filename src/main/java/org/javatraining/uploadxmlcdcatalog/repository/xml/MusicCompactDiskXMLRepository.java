package org.javatraining.uploadxmlcdcatalog.repository.xml;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.repository.MusicCompactDiskRepository;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogReader;
import org.javatraining.uploadxmlcdcatalog.storage.fs.xml.CatalogReaderFromXML;
import org.javatraining.uploadxmlcdcatalog.storage.fs.xml.CatalogWriterToXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Репозиторий для работы с объектами MusicCompactDisk. При создании репозиторий считывает данные
 * о компакт дисках из файла на диске и сохраняет во внутреннем хранилище.
 * Класс реализует типовые CRUD-операции над данными.
 * <p>
 * При уничтожении записывает данные в xml-файл на диске.
 */

@Repository
public class MusicCompactDiskXMLRepository implements MusicCompactDiskRepository {
    private Logger logger = LoggerFactory.getLogger(MusicCompactDiskXMLRepository.class);
    private final Lock saveAllLock = new ReentrantLock();

    @Value("${app.collectionFile}")
    private String DISK_CATALOG_FILE_NAME;

    @Value("${app.pageSize}")
    private int PAGE_SIZE;

    private final Map<String, MusicCompactDisk> storage = new HashMap<>();

    @PostConstruct
    private void postConstruct() {
        logger.info("Инициализация репозитория");
        Resource resource = new FileSystemResource(DISK_CATALOG_FILE_NAME);
        if (!resource.exists()) {
            try (PrintWriter writer = new PrintWriter(new File(DISK_CATALOG_FILE_NAME))) {
                writer.write("<CATALOG>" + "\n" + "</CATALOG>");
                logger.error("Файл каталога не найден. Создан новый файл " + DISK_CATALOG_FILE_NAME);
            } catch (IOException e) {
                logger.error("Ошибка создания файла " + DISK_CATALOG_FILE_NAME);
                e.printStackTrace();
            }
        } else {
            try (InputStream is = resource.getInputStream()) {
                CatalogReader<MusicCompactDisk> xmlReader = new CatalogReaderFromXML();
                saveAll(xmlReader.readFromInpuStream(is));
            } catch (IOException e) {
                logger.error("Ошибка чтения InputStream с каталогом");
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MusicCompactDisk> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<MusicCompactDisk> findByPage(int page) {
        return storage.values()
                .stream().skip((page - 1) * PAGE_SIZE)
                .limit(PAGE_SIZE).collect(Collectors.toList());
    }

    @Override
    public MusicCompactDisk findByTitle(String title) {
        return storage.getOrDefault(title, null);
    }

    @Override
    public MusicCompactDisk save(MusicCompactDisk musicCompactDisk) {
        return storage.put(musicCompactDisk.getTitle(), musicCompactDisk);
    }

    @Override
    public void delete(MusicCompactDisk musicCompactDisk) {
        storage.remove(musicCompactDisk.getTitle());
    }

    @Override
    public void saveAll(List<MusicCompactDisk> list) {
        try {
            saveAllLock.tryLock(300, TimeUnit.SECONDS);
            Map<String, MusicCompactDisk> map = list.stream()
                    .collect(Collectors.toMap(MusicCompactDisk::getTitle, disk -> disk));
            storage.putAll(map);
        } catch (InterruptedException ex) {
            logger.error("Ошибка организации доступа к хранилищу каталога");
        } finally {
            saveAllLock.unlock();
        }
    }

    @Override
    public int size() {
        return storage.size();
    }

    @PreDestroy
    private void preDestroy() {
        logger.info("Сохранение репозитория при завершении программы");
        CatalogWriterToXML xmlWriter = new CatalogWriterToXML();
        File targetFile = new File(DISK_CATALOG_FILE_NAME);
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e) {
            logger.error("Ошибка записи каталога");
            e.printStackTrace();
        }
        xmlWriter.writeToOutputStream(findAll(), outStream);
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
