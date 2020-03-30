package com.xrq.tv.down;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 765773123
 * @date 2018/11/22
 * 线程池，执行耗时任务
 */
public class ThreadPool {
    private ThreadPoolExecutor executor;
    /**
     * 核心线程数
     */
    private int corePoolSize = 5;
    /**
     * 最大线程数
     */
    private int maximumPoolSize = 20;
    /**
     * 非核心线程的超时时长，当系统非核心线程闲置时间超过keepAliveTime之后则会被回收
     * 如果ThreadPoolExecutor的allowCoreThreadTimeOut属性设置为true，则该参数也表示核心线程的超时时长
     */
    private long keepAliveTime = 1000 * 20;
    /**
     * 第三个参数的单位，有纳秒、微秒、毫秒、秒、分、时、天等
     */
    private TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务。存储在这里的任务是由ThreadPoolExecutor的execute方法提交来的。
     * 有四个实现：
     * ArrayBlockingQueue：这个表示一个规定了大小的BlockingQueue，ArrayBlockingQueue的构造函数接受一个int类型的数据，该数据表示BlockingQueue的大小，存储在ArrayBlockingQueue中的元素按照FIFO（先进先出）的方式来进行存取。
     * LinkedBlockingQueue：这个表示一个大小不确定的BlockingQueue，在LinkedBlockingQueue的构造方法中可以传一个int类型的数据，这样创建出来的LinkedBlockingQueue是有大小的，也可以不传，不传的话，LinkedBlockingQueue的大小就为Integer.MAX_VALUE
     * PriorityBlockingQueue：这个队列和LinkedBlockingQueue类似，不同的是PriorityBlockingQueue中的元素不是按照FIFO来排序的，而是按照元素的Comparator来决定存取顺序的（这个功能也反映了存入PriorityBlockingQueue中的数据必须实现了Comparator接口）
     * SynchronousQueue：这个是同步Queue，属于线程安全的BlockingQueue的一种，在SynchronousQueue中，生产者线程的插入操作必须要等待消费者线程的移除操作，Synchronous内部没有数据缓存空间，因此我们无法对SynchronousQueue进行读取或者遍历其中的数据，
     * 元素只有在你试图取走的时候才有可能存在。我们可以理解为生产者和消费者互相等待，等到对方之后然后再一起离开。
     */
    private BlockingQueue<Runnable> workQueue;
    /**
     * 为线程池提供创建新线程的功能
     */
    private ThreadFactory threadFactory;
    /**
     * 拒绝策略，当线程无法执行新任务时（一般是由于线程池中的线程数量已经达到最大数或者线程池关闭导致的），
     * 默认情况下，当线程池无法处理新线程时，会抛出一个RejectedExecutionException。
     */
    private RejectedExecutionHandler handler;

    private ThreadPool() {

    }

    public static ThreadPool getInstance() {
        return ThreadPoolHolder.THREAD_POOL;
    }

    private static class ThreadPoolHolder {
        private static final ThreadPool THREAD_POOL = new ThreadPool();
    }

    private void init() {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            synchronized (ThreadPool.class) {
                if (executor == null || executor.isShutdown() || executor.isTerminated()) {
                    workQueue = new LinkedBlockingDeque<>(5);
                    threadFactory = Executors.defaultThreadFactory();
                    handler = new ThreadPoolExecutor.DiscardPolicy();
                    executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
    }

    /**
     * 执行一个任务
     *
     * @param runnable 任务
     */
    public void execute(Runnable runnable) {
        init();
        executor.execute(runnable);
    }

    /**
     * 提交任务
     */
    public Future submit(Runnable task) {
        init();
        return executor.submit(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        init();
        executor.remove(task);
    }

    /**
     * 停止一个任务
     */
    public void stop(Runnable tas) {
        init();
    }

    /**
     * 全部暂停
     */
    public void stopAll() {
        init();
    }
    /**
     * 关闭线程池
     */

}
