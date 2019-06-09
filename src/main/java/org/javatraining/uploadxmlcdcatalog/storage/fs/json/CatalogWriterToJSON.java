package org.javatraining.uploadxmlcdcatalog.storage.fs.json;

import org.javatraining.uploadxmlcdcatalog.model.MusicCompactDisk;
import org.javatraining.uploadxmlcdcatalog.storage.fs.CatalogWriter;

import java.io.OutputStream;
import java.util.List;

public class CatalogWriterToJSON implements CatalogWriter<MusicCompactDisk> {
    @Override
    public void writeToOutputStream(List<MusicCompactDisk> list, OutputStream outputStream) {

    }
}
