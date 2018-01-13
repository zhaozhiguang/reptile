package com.zhaozhiguang.component.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 读取文件,写入文件
 * @author zhaozhiguang
 */
public class ScannerUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScannerUtils.class);

    private static String path;

    static {
        path = LoadPropertiesUtils.load().getProperty("down.url.dir");
        if(path==null) logger.error("读取url的文件目录没有设置");
    }

    /**
     * 扫描url
     * @return
     */
    public static List<String> scanUrl() {
        List<String> urls = new LinkedList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
            while (scanner.hasNext()){
                urls.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            logger.error("文件不存在", e);
        } finally {
            if(scanner!=null)
                scanner.close();
        }
        return urls;
    }

    /**
     * 写入url
     * @param text
     */
    public static void writeUrl(String text) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path),true));
            writer.newLine();
            writer.append(text);
            writer.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("写入url发生异常", e);
        } finally {
            if(writer!=null)
                try {
                    writer.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
        }

    }
}
