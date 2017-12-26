package com.zhaozhiguang.component.parse.xp1024;


import com.zhaozhiguang.component.parse.HtmlParseResolver;
import com.zhaozhiguang.component.threads.CustomThreadPoolFactory;
import com.zhaozhiguang.component.threads.ThreadPoolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

public class NetFriendFakeListResolver implements HtmlParseResolver,Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NetFriendFakeListResolver.class);

    private BlockingQueue<String> queue;

    private ExecutorService executorService;

    public NetFriendFakeListResolver() {
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
            String text = queue.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("解析网页发生异常-[12301]");
        }
    }
}
