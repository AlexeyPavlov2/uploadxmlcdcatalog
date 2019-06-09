package org.javatraining.uploadxmlcdcatalog.controller;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.repository.MusicCompactDiskRepository;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogReader;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogWriter;
import org.javatraining.uploadxmlcdcatalog.storage.fs.xml.CatalogReaderFromXML;
import org.javatraining.uploadxmlcdcatalog.storage.fs.xml.CatalogWriterToXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Value("${app.saveUploadedFile}")
    private boolean SAVE_UPLOADED_FILE;

    @Value("${app.collectionFile}")
    private String DISK_CATALOG_FILE_NAME;

    private final MusicCompactDiskRepository diskRepository;

    public UploadController(MusicCompactDiskRepository diskRepository) {
        this.diskRepository = diskRepository;
    }

    @GetMapping("/upload-xml")
    public String index() {
        logger.info("Страница закачки файла на сервер");
        return "upload-xml";
    }

    @PostMapping("/process-xml")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        logger.info("Закачка файла на сервер");

        if (file.isEmpty()) {
            setRedirectAttributes(redirectAttributes, "Выберите XML-файл для загрузки", false);
            return "redirect:uploadStatus";
        }

        try {
            byte[] bytes = file.getBytes();

            // Сохраним файл в директории для временных файлов, если установлен соответствующий параметр
            if (SAVE_UPLOADED_FILE) {
                Path path = Paths.get(System.getProperty("java.io.tmpdir") +
                        "UUID" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename());
                Files.write(path, bytes);
            }

            // Прочитаем данные и поместим в репозиторий. Диски с тем же именем будут замещены.

            CatalogReader<MusicCompactDisk> xmlReader = new CatalogReaderFromXML();
            List<MusicCompactDisk> list = xmlReader.readFromInpuStream(file.getInputStream());
            diskRepository.saveAll(list);

            // Запишем обновленный каталог на диск
            File diskFile = new File(DISK_CATALOG_FILE_NAME);
            if (!diskFile.exists()) {
                logger.error("Файл " + DISK_CATALOG_FILE_NAME + " не найден");
                throw new FileNotFoundException("Файл " + DISK_CATALOG_FILE_NAME + " не найден");
            }

            //TODO Thread?
            CatalogWriter<MusicCompactDisk> xmlWriter = new CatalogWriterToXML();
            OutputStream outputStream = new FileOutputStream(diskFile);
            xmlWriter.writeToOutputStream(diskRepository.findAll(), outputStream);
            outputStream.close();

        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода XML-файла - '" + file.getOriginalFilename() + "'");
            setRedirectAttributes(redirectAttributes, "Ошибка ввода-вывода XML-файла - '" + file.getOriginalFilename() + "'", true);
            e.printStackTrace();
        }
        setRedirectAttributes(redirectAttributes, "'" + file.getOriginalFilename() + "'" + " - файл успешно загружен", false);
        return "redirect:uploadStatus";
    }

    @GetMapping("uploadStatus")
    public String uploadStatus() {
        logger.info("Переход на страницу uploadStatus");
        return "uploadStatus";
    }

    private void setRedirectAttributes(RedirectAttributes attributes, String message, boolean error) {
        attributes.addFlashAttribute("message", message);
        attributes.addFlashAttribute("error", error);
    }

}
