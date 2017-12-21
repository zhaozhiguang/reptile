package com.zhaozhiguang.component.spider;

import com.zhaozhiguang.component.threads.CustomThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * 简单解析图片下载实现
 */
public class SimpleImageParse implements ImageParseable {

    private ExecutorService executorService;

    public SimpleImageParse(){
        executorService = CustomThreadPoolFactory.build(CustomThreadPoolFactory.ThreadPoolType.FIXED_THREADPOOL);
    }

    @Override
    public void parseImage(String url) {

    }
}
