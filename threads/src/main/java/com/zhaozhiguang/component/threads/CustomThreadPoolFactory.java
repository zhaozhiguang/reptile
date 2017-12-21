package com.zhaozhiguang.component.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义线程池
 */
public class CustomThreadPoolFactory {

    /**
     * 线程池类型
     */
    public enum ThreadPoolType {
        /**
         * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
         */
        CACHED_THREADPOOL,
        /**
         * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
         */
        FIXED_THREADPOOL,
        /**
         * 创建一个定长线程池，支持定时及周期性任务执行。
         */
        SCHEDULED_THREADPOOL,
        /**
         * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
         */
        SINGLETHREAD_THREADPOOL
    }

    private CustomThreadPoolFactory(){}

    private static volatile CustomThreadPoolFactory factory = null;

    /*public static CustomThreadPoolFactory getInstance(){
        if(factory==null){
            synchronized (CustomThreadPoolFactory.class){
                if(factory==null){
                    factory = new CustomThreadPoolFactory();
                }
            }
        }
        return factory;
    }*/

    /**
     * 线程池引用
     */
    private volatile static ExecutorService cached_executor = null;
    private volatile static ExecutorService fixed_executor = null;
    private volatile static ExecutorService scheduled_executor = null;
    private volatile static ExecutorService singlethread_executor = null;

    public static ExecutorService build(ThreadPoolType type) {
        if(type == null) throw new RuntimeException("线程池类型为空");
        switch (type){
            case CACHED_THREADPOOL:
                if(cached_executor==null){
                    synchronized (CustomThreadPoolFactory.cached_executor){
                        if(cached_executor==null){
                            cached_executor = Executors.newCachedThreadPool();
                        }
                    }
                }
                return cached_executor;
            case FIXED_THREADPOOL:
                if(fixed_executor==null){
                    synchronized (CustomThreadPoolFactory.fixed_executor){
                        if(fixed_executor==null){
                            fixed_executor = Executors.newFixedThreadPool(100);
                        }
                    }
                }
                return fixed_executor;
            case SCHEDULED_THREADPOOL:
                if(scheduled_executor==null){
                    synchronized (CustomThreadPoolFactory.scheduled_executor){
                        if(scheduled_executor==null){
                            scheduled_executor = Executors.newScheduledThreadPool(100);
                        }
                    }
                }
                return scheduled_executor;
            case SINGLETHREAD_THREADPOOL:
                if(singlethread_executor==null){
                    synchronized (CustomThreadPoolFactory.singlethread_executor){
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
