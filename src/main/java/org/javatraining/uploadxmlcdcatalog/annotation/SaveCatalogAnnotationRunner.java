package org.javatraining.uploadxmlcdcatalog.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SaveCatalogAnnotationRunner {
    private Logger logger = LoggerFactory.getLogger(SaveCatalogAnnotationRunner.class);
    public void parse(Class<?> clazz) throws Exception {
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(SaveCatalog.class)) {
                try {
                    logger.info("Запуск обработчика аннотации");
                    method.invoke(null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
