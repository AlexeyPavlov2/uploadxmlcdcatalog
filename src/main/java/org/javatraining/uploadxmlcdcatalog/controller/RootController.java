package org.javatraining.uploadxmlcdcatalog.controller;

import org.javatraining.uploadxmlcdcatalog.repository.xml.MusicCompactDiskXMLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RootController {
    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    @Value("${app.pageSize}")
    private int PAGE_SIZE;

    private final MusicCompactDiskXMLRepository diskRepository;

    public RootController(MusicCompactDiskXMLRepository repository) {
        this.diskRepository = repository;
    }

    @GetMapping("/")
    public String RootIndex(@RequestParam(value = "page", required = false) Integer page, Model model) {
        logger.info("Основная страница");
        int currentPage = page != null ? page : 1;
        model.addAttribute("musicCompactDisks", diskRepository.findByPage(currentPage));
        model.addAttribute("pageCount",
                Math.ceil(diskRepository.size() / (double) PAGE_SIZE));
        model.addAttribute("currentPage", currentPage);
        return "index";
    }





}
