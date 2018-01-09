package com.zhaozhiguang.component.parse;

import com.zhaozhiguang.component.threads.CustomThreadPoolFactory;
import com.zhaozhiguang.component.threads.ThreadPoolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class AbstractHtmlParseResolver implements HtmlParseResolver,Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHtmlParseResolver.class);

    protected BlockingQueue<String> queue;

    protected ExecutorService executorService;

    /**
     * junit测试使用
     * 由于junit不会管除他的主线程之外的线程结果,所以需要同步
     */
    protected CountDownLatch latch;

    public AbstractHtmlParseResolver() {
        this.queue = new LinkedBlockingDeque<>(100);
        executorService = CustomThreadPoolFactory.build(ThreadPoolType.FIXED_THREADPOOL);
    }

    @Override
    public void parse(String url) {
        try {
            queue.put(url);
            executorService.execute(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("解析网页发生异常");
        }
    }

    @Override
    public void run() {
        try {
            task(queue.take());
            if(latch!=null) latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解析网页发生异常-[12301]");
        }
    }

    /**
     * 实现解析
     * @param url
     */
    protected abstract void task(String url) throws Exception;

}