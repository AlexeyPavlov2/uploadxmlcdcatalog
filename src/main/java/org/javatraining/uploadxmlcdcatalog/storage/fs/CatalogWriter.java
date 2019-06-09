package org.javatraining.uploadxmlcdcatalog.storage.fs;

import java.io.OutputStream;
import java.util.List;

/** Интерфейс для записи данных о CD (или что-то еще) в OutputStream
 *
 */

public interface CatalogWriter<T>  {

    void writeToOutputStream(List<T> list, OutputStream outputStream);

}
