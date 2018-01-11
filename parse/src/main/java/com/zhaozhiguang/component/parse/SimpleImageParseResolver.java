package com.zhaozhiguang.component.parse;

import com.zhaozhiguang.component.common.FileUtils;
import com.zhaozhiguang.component.common.PatternUtils;
import com.zhaozhiguang.component.parse.xp1024.ListModel;
import com.zhaozhiguang.component.threads.CustomThreadPoolFactory;
import com.zhaozhiguang.component.threads.ThreadPoolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 简单图片下载处理实现
 */
public class SimpleImageParseResolver implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SimpleImageParseResolver.class);

    private static String dir;

    /**
     * junit测试使用
     * 由于junit不会管除他的主线程之外的线程结果,所以需要同步
     */
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

    public void setDir(String dir) {
        SimpleImageParseResolver.dir = dir;
    }

    private BlockingQueue<ListModel> queue;

    private ExecutorService executorService;

    public SimpleImageParseResolver(){
        this.queue = new LinkedBlockingDeque<>(100);
        executorService = CustomThreadPoolFactory.build(ThreadPoolType.FIXED_THREADPOOL);
    }

    public SimpleImageParseResolver(CountDownLatch latch){
        this();
        this.latch = latch;
    }

    public SimpleImageParseResolver(String dir){
        this();
        SimpleImageParseResolver.dir = dir;
    }

    public void parse(ListModel model) {
        try {
            queue.put(model);
            executorService.execute(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("解析图片发生异常");
        }
    }

    @Override
    public void run() {
        try {
            ListModel model = queue.take();
            FileUtils.copyURLToFile(new URL(model.getUrl()),new File(dir +model.getTitle()+"/"+ UUID.randomUUID() + "."+ PatternUtils.getSuffixtoUrl(model.getUrl())));
            if(latch!=null) latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("保存图片发生异常-[12035]");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("保存图片到文件发生异常-[12034]");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存图片到文件发生异常-[12033]");
        }
    }
}
