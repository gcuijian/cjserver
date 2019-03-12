package com.cjserver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>启动类、配置类</p>
 * @ClassName Start
 * @Description 
 * @Author Cui Jian
 * @version
 * @Date 2019年1月17日 下午3:18:16
 */
public class Start {

	// 配置服务器本地路径
	public static final String ROOT_PATH = "E:/myserver/";
	
	// 配置端口号
	public static final int PORT = 8989;
	
	// 默认根目录访问文件
	public static final String DEFAULT_PAGE = "index.html";
	
	// 线程池对象
	public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;
	
	// CPU核心数
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	// 核心线程数
	private static final int CORE_POOL_SIZE = Math.max(4, Math.min(CPU_COUNT - 1, 5));
	// 线程池中最大的线程数
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 2;
	// 线程的存活时间，没事干的时候，空闲的时间
    private static final long KEEP_ALIVE_TIME = 60; 
    // 线程缓存队列
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(10);
	
	// 初始化线程池
    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    // 较低优先级、提高运行
                    thread.setPriority(4);
                    return thread;
                }
            });
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }
    
	public static void main(String[] args) {
		Logger.log("start", "Cj Server will start!");
		new Server().init();
	}
	
}
