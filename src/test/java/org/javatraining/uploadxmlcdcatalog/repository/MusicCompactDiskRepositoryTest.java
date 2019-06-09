package org.javatraining.uploadxmlcdcatalog.repository;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.repository.xml.MusicCompactDiskXMLRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicCompactDiskRepositoryTest {

    @Autowired
    private MusicCompactDiskXMLRepository diskRepository;

    @Value("${app.pageSize}")
    private int PAGE_SIZE;

    @Test
    public void findAll() {
        Assert.assertTrue(diskRepository.findAll().size() > 0);
    }

    @Test
    public void findByPage() {
        assertEquals(diskRepository.findByPage(1).size(), PAGE_SIZE);
    }

    @Test
    public void findByTitle() {
        assertNotNull(diskRepository.findByTitle("Hide your heart"));
    }

    @Test
    public void save() {
        MusicCompactDisk disk =
                diskRepository.save(new MusicCompactDisk("New Title", "New Artist",
                "USA", "Some Company", BigDecimal.valueOf(33.44), 1995));
        assertNotNull(diskRepository.findByTitle("New Title"));
    }

    @Test
    public void delete() {
        int initSize = diskRepository.size();
        MusicCompactDisk disk = diskRepository.findByTitle("1999 Grammy Nominees");
        assertNotNull(disk);
        diskRepository.delete(disk);
        assertTrue(initSize > diskRepository.size());
    }

    @Test
    public void size() {
        assertTrue(diskRepository.size() > 0);
    }
}