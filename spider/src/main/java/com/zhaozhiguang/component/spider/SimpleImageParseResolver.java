package com.zhaozhiguang.component.spider;

import com.zhaozhiguang.component.common.FileUtils;
import com.zhaozhiguang.component.common.PatternUtils;
import com.zhaozhiguang.component.threads.CustomThreadPoolFactory;
import com.zhaozhiguang.component.threads.ThreadPoolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 简单图片下载处理实现
 */
public class SimpleImageParseResolver implements FileParseResolver,Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SimpleImageParseResolver.class);

    private static String dir;

    private CountDownLatch latch;

    static {
        Properties properties = new Properties();
        InputStream inputStream = SimpleImageParseResolver.class.getResourceAsStream("/config.properties");
        try {
            properties.load(inputStream);
            dir = properties.getProperty("down.image.file.dir");
            if(dir==null) logger.error("下载图片的目录没有设置");
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
    }

    private BlockingQueue<String> queue;

    private ExecutorService executorService;

    public SimpleImageParseResolver(BlockingQueue<String> queue){
        this.queue = queue;
        executorService = CustomThreadPoolFactory.build(ThreadPoolType.FIXED_THREADPOOL);
        executorService.execute(this);
    }

    public SimpleImageParseResolver(BlockingQueue<String> queue, CountDownLatch latch){
        this(queue);
        this.latch = latch;
    }

    @Override
    public void parse(String url) {
        try {
            FileUtils.copyURLToFile(new URL(url),new File(dir + UUID.randomUUID() + "."+ PatternUtils.getSuffixtoUrl(url)));
            logger.debug("文件地址:{}",url);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存图片到文件发生异常-[12034]");
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                parse(queue.take());
                if(latch!=null) latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("保存图片发生异常-[12035]");
            }
        }
    }
}
