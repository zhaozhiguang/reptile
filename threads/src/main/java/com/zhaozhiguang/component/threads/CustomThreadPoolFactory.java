package com.zhaozhiguang.component.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义线程池
 */
public class CustomThreadPoolFactory {

    private CustomThreadPoolFactory(){}

    /**
     * 线程池引用
     */
    private volatile static ExecutorService cached_executor = null;
    private static Object cached_obj = new Object();
    private volatile static ExecutorService fixed_executor = null;
    private static Object fixed_obj = new Object();
    private volatile static ExecutorService scheduled_executor = null;
    private static Object scheduled_obj = new Object();
    private volatile static ExecutorService singlethread_executor = null;
    private static Object singlethread_obj = new Object();

    public static ExecutorService build(ThreadPoolType type) {
        if(type == null) throw new RuntimeException("线程池类型为空");
        switch (type){
            case CACHED_THREADPOOL:
                if(cached_executor==null){
                    synchronized (CustomThreadPoolFactory.cached_obj){
                        if(cached_executor==null){
                            cached_executor = Executors.newCachedThreadPool();
                        }
                    }
                }
                return cached_executor;
            case FIXED_THREADPOOL:
                if(fixed_executor==null){
                    synchronized (CustomThreadPoolFactory.fixed_obj){
                        if(fixed_executor==null){
                            fixed_executor = Executors.newFixedThreadPool(100);
                        }
                    }
                }
                return fixed_executor;
            case SCHEDULED_THREADPOOL:
                if(scheduled_executor==null){
                    synchronized (CustomThreadPoolFactory.scheduled_obj){
                        if(scheduled_executor==null){
                            scheduled_executor = Executors.newScheduledThreadPool(100);
                        }
                    }
                }
                return scheduled_executor;
            case SINGLETHREAD_THREADPOOL:
                if(singlethread_executor==null){
                    synchronized (CustomThreadPoolFactory.singlethread_obj){
                        if(singlethread_executor==null){
                            singlethread_executor = Executors.newSingleThreadExecutor();
                        }
                    }
                }
                return singlethread_executor;
            default:
                return null;
        }
    }
}
