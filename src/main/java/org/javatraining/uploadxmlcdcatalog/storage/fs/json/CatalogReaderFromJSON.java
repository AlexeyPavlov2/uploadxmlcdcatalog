package org.javatraining.uploadxmlcdcatalog.storage.fs.json;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogReader;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class CatalogReaderFromJSON implements CatalogReader<MusicCompactDisk> {
    @Override
    public List<MusicCompactDisk> readFromInpuStream(InputStream inputStream) {
        return Collections.emptyList();
    }
}
