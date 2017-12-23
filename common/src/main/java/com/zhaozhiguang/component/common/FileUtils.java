package com.zhaozhiguang.component.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件处理工具类
 * @author zhaozhiguang
 */
public class FileUtils {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36";

    public static void copyURLToFile(URL source, File destination) throws IOException {
        copyURLToFile(source, destination, null, null);
    }

    public static void copyURLToFile(URL source, File destination,
                                     Integer connectionTimeout, Integer readTimeout) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) source.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        if(connectionTimeout!=null) connection.setConnectTimeout(connectionTimeout);
        if(readTimeout!=null) connection.setReadTimeout(readTimeout);
        connection.connect();
        InputStream input = connection.getInputStream();
        copyInputStreamToFile(input, destination);
    }

    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtils.copy(source, output);
                output.close(); // don't swallow close Exception if copy completes normally
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(source);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

}
