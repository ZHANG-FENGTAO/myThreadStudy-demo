package com.example.demo;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的监控
 *
 * @author zft
 * @date 2018/10/23.
 */
public class ExtThreadPool {

    public static class MyThread implements Runnable {

        public String name;

        MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("正在执行：" + "ThreadID:" + Thread.currentThread().getId() + ", TaskName:" + name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new DefaultThreadFactory("default")) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("准备执行" + ((MyThread) r).name);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("执行完毕" + ((MyThread) r).name);
            }

            @Override
            protected void terminated() {
                System.out.println("线程池退出");
            }
        };

        for (int i = 0; i < 5; i++) {
            MyThread myThread = new MyThread("Task-Name-" + i);
            // 注意: 这里必须是execute 提交 而不是submit
            executor.execute(myThread);
            Thread.sleep(10);
        }
        executor.shutdown();
    }

}
