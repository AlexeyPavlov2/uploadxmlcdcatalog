package org.javatraining.uploadxmlcdcatalog.repository;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;

import java.util.List;

public interface MusicCompactDiskRepository {

    List<MusicCompactDisk> findAll();

    List<MusicCompactDisk> findByPage(int page);

    MusicCompactDisk findByTitle(String title);

    MusicCompactDisk save(MusicCompactDisk musicCompactDisk);

    void saveAll(List<MusicCompactDisk> list);

    void delete(MusicCompactDisk musicCompactDisk);

    int size();

}
