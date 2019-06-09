package org.javatraining.uploadxmlcdcatalog.storage.fs;

import java.io.InputStream;
import java.util.List;

/** Интерфейс для считывания данных о CD (или что-то еще) с InputStream
 *
 */

public interface  CatalogReader<T> {

    List<T> readFromInpuStream(InputStream inputStream);

}
