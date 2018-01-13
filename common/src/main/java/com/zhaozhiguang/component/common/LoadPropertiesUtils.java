package com.zhaozhiguang.component.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadPropertiesUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoadPropertiesUtils.class);

    public static Properties load() {
        Properties properties = new Properties();
        InputStream inputStream = LoadPropertiesUtils.class.getResourceAsStream("/config.properties");
        try {
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("加载配置文件异常");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
