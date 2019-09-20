package com.fangdd.framework.config;

import com.fangdd.logtrace.async.TraceIdExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    @Value("${exector.default.querySize}")
    private int queueSize = 1024 * 256;

    @Value("${exector.default.coreSize}")
    private int coreSize = 0;

    @Value("${exector.default.maxSize}")
    private int maxSize = 100;

    @Value("${exector.default.keepAliveSeconds}")
    private int keepAliveTime = 30;

    @Value("${exector.default.namePrefix}")
    private String namePrefix = "fdd-worker-";

    private BlockingQueue<Runnable> queue;

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        if (coreSize <= 0) {
            coreSize = Runtime.getRuntime().availableProcessors();
        }

        queue = new LinkedBlockingQueue<Runnable>(queueSize);
        return new TraceIdExecutorService(new ThreadPoolExecutor(
                coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, queue,
                new ThreadFactory() {
                    private AtomicInteger seq = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName(namePrefix + seq.getAndAdd(1));
                        return t;
                    }
                },
                new ThreadPoolExecutor.DiscardPolicy()
        ));
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                LOGGER.error("async error, method: {}, params : {}" + method.getName(), params, ex);
            }
        };
    }

    @PreDestroy
    public void preDestroy() {
        while (queue.peek() != null) {
            LOGGER.info("default executor queue size: " + queue.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
        }

        LOGGER.info("default executor queue is clear!");
    }
}
