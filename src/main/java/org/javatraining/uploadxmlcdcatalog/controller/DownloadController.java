package org.javatraining.uploadxmlcdcatalog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class DownloadController {
    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Value("${app.collectionFile}")
    private String DISK_CATALOG_FILE_NAME;

    @GetMapping(value = "/download-xml")
    public @ResponseBody
    Resource downloadFile(HttpServletResponse response) throws IOException {
        logger.info("Скачивание объединенного файла");
        File file = new File(DISK_CATALOG_FILE_NAME);
        if (!file.exists()) {
            logger.error("Файл " + DISK_CATALOG_FILE_NAME + " не найден");
            throw new FileNotFoundException("Файл " + DISK_CATALOG_FILE_NAME + " не найден");
        }
        Resource resource = new FileSystemResource(DISK_CATALOG_FILE_NAME);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return resource;
    }

}
